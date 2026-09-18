package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.AddCategoriaERetornarIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllEstabelecimentosUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasByEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoMonthlyHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.LinkEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateApelidoEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCategoriaEstabelecimentoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.EstablishmentDetailState
import com.rogue.shopcontrol.utils.parseDataCompra
import com.rogue.shopcontrol.utils.toChartEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstablishmentDetailViewModel(
    private val estabelecimentoId: Long,
    getEstabelecimentoById: GetEstabelecimentoByIdUseCase,
    getEstabelecimentoMonthlyHistory: GetEstabelecimentoMonthlyHistoryUseCase,
    getComprasByEstabelecimento: GetComprasByEstabelecimentoUseCase,
    private val updateApelidoEstabelecimento: UpdateApelidoEstabelecimentoUseCase,
    getAllEstabelecimentos: GetAllEstabelecimentosUseCase,
    private val linkEstabelecimento: LinkEstabelecimentoUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase,
    getCategorias: GetCategoriasUseCase,
    private val updateCategoriaEstabelecimento: UpdateCategoriaEstabelecimentoUseCase,
    private val addCategoriaERetornarId: AddCategoriaERetornarIdUseCase
) : ViewModel() {


    private var comprasOriginais: List<CompraCompleta> = emptyList()

    private val _state =
        MutableStateFlow(
            EstablishmentDetailState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(dateRange = getHomeDateRange().first())

            aplicarFiltroMes()

        }

        viewModelScope.launch {

            getEstabelecimentoById(estabelecimentoId).collect { estabelecimento ->

                _state.value =
                    _state.value.copy(
                        estabelecimento = estabelecimento,
                        isLoading = false
                    )

            }

        }

        viewModelScope.launch {

            getEstabelecimentoMonthlyHistory(estabelecimentoId).collect { historico ->

                _state.value =
                    _state.value.copy(
                        chartEntries = historico.map { it.toChartEntry() }
                    )

            }

        }

        viewModelScope.launch {

            getComprasByEstabelecimento(estabelecimentoId).collect { compras ->

                comprasOriginais = compras

                aplicarFiltroMes()

            }

        }

        viewModelScope.launch {

            getAllEstabelecimentos().collect { lista ->

                _state.value =
                    _state.value.copy(
                        estabelecimentosComCnpj = lista.filter {
                            it.cnpj.isNotBlank() && it.id != estabelecimentoId
                        }
                    )

            }

        }

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                _state.value =
                    _state.value.copy(categorias = categorias)

            }

        }

    }


    fun onShowLinkPicker() {

        _state.value =
            _state.value.copy(showLinkPicker = true)

    }


    fun onDismissLinkPicker() {

        _state.value =
            _state.value.copy(showLinkPicker = false)

    }


    fun onLinkTargetSelected(comCnpjId: Long) {

        val origemCategoriaId = _state.value.estabelecimento?.categoriaId
        val destinoCategoriaId = _state.value.estabelecimentosComCnpj.firstOrNull { it.id == comCnpjId }?.categoriaId

        if (origemCategoriaId != null && destinoCategoriaId != null && origemCategoriaId != destinoCategoriaId) {

            _state.value =
                _state.value.copy(
                    showLinkPicker = false,
                    showCategoriaConflictDialog = true,
                    pendingLinkTargetId = comCnpjId,
                    categoriaConflictOrigemId = origemCategoriaId,
                    categoriaConflictDestinoId = destinoCategoriaId
                )

        } else {

            _state.value =
                _state.value.copy(
                    showLinkPicker = false,
                    showLinkConfirm = true,
                    pendingLinkTargetId = comCnpjId
                )

        }

    }


    fun onCategoriaConflictResolved(categoriaEscolhidaId: Long) {

        _state.value =
            _state.value.copy(
                showCategoriaConflictDialog = false,
                showLinkConfirm = true,
                categoriaEscolhidaParaMerge = categoriaEscolhidaId
            )

    }


    fun onDismissCategoriaConflictDialog() {

        _state.value =
            _state.value.copy(
                showCategoriaConflictDialog = false,
                pendingLinkTargetId = null,
                categoriaConflictOrigemId = null,
                categoriaConflictDestinoId = null
            )

    }


    fun onDismissLinkConfirm() {

        _state.value =
            _state.value.copy(
                showLinkConfirm = false,
                pendingLinkTargetId = null,
                categoriaEscolhidaParaMerge = null
            )

    }


    fun onConfirmLink() {

        val targetId = _state.value.pendingLinkTargetId ?: return
        val categoriaEscolhida = _state.value.categoriaEscolhidaParaMerge

        viewModelScope.launch {

            linkEstabelecimento(estabelecimentoId, targetId)

            if (categoriaEscolhida != null) {
                updateCategoriaEstabelecimento(targetId, categoriaEscolhida)
            }

            _state.value =
                _state.value.copy(
                    showLinkConfirm = false,
                    isLinked = true
                )

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltroMes()

    }


    fun onEditClick() {

        _state.value =
            _state.value.copy(
                showEditDialog = true,
                editApelidoText = _state.value.estabelecimento?.apelido ?: ""
            )

    }


    fun onApelidoTextChanged(texto: String) {

        _state.value =
            _state.value.copy(
                editApelidoText = texto
            )

    }


    fun onSaveApelido() {

        viewModelScope.launch {

            val novoApelido =
                _state.value.editApelidoText.trim().ifBlank { null }

            updateApelidoEstabelecimento(estabelecimentoId, novoApelido)

            _state.value =
                _state.value.copy(
                    showEditDialog = false
                )

        }

    }


    fun onEditDialogDismiss() {

        _state.value =
            _state.value.copy(
                showEditDialog = false
            )

    }


    fun onShowCategoryPicker() {

        _state.value =
            _state.value.copy(showCategoryPicker = true)

    }


    fun onDismissCategoryPicker() {

        _state.value =
            _state.value.copy(showCategoryPicker = false)

    }


    fun onCategoriaSelecionada(categoriaId: Long) {

        viewModelScope.launch {

            updateCategoriaEstabelecimento(estabelecimentoId, categoriaId)

            _state.value =
                _state.value.copy(showCategoryPicker = false)

        }

    }


    fun onCreateCategoria(nome: String) {

        viewModelScope.launch {

            val categoriaId = addCategoriaERetornarId(nome)

            updateCategoriaEstabelecimento(estabelecimentoId, categoriaId)

            _state.value =
                _state.value.copy(showCategoryPicker = false)

        }

    }


    private fun aplicarFiltroMes() {

        val range = _state.value.dateRange

        val filtradas =
            comprasOriginais.filter { compraCompleta ->

                val data =
                    parseDataCompra(compraCompleta.compra.dataParaFiltro)

                data != null && data in range

            }

        _state.value =
            _state.value.copy(
                compras = filtradas
            )

    }

}
