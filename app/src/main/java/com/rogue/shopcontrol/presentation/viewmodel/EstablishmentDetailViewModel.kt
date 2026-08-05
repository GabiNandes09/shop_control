package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.domain.usecase.GetComprasByEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentoMonthlyHistoryUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateApelidoEstabelecimentoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.EstablishmentDetailState
import com.rogue.shopcontrol.utils.parseDataCompra
import com.rogue.shopcontrol.utils.toChartEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

class EstablishmentDetailViewModel(
    private val estabelecimentoId: Long,
    getEstabelecimentoById: GetEstabelecimentoByIdUseCase,
    getEstabelecimentoMonthlyHistory: GetEstabelecimentoMonthlyHistoryUseCase,
    getComprasByEstabelecimento: GetComprasByEstabelecimentoUseCase,
    private val updateApelidoEstabelecimento: UpdateApelidoEstabelecimentoUseCase
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

    }


    fun onPreviousMonth() {

        _state.value =
            _state.value.copy(
                selectedMonth = _state.value.selectedMonth.minusMonths(1)
            )

        aplicarFiltroMes()

    }


    fun onNextMonth() {

        _state.value =
            _state.value.copy(
                selectedMonth = _state.value.selectedMonth.plusMonths(1)
            )

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


    private fun aplicarFiltroMes() {

        val mes = _state.value.selectedMonth

        val filtradas =
            comprasOriginais.filter { compraCompleta ->

                val data =
                    parseDataCompra(compraCompleta.compra.dataCompra)

                data != null && YearMonth.from(data) == mes

            }

        _state.value =
            _state.value.copy(
                compras = filtradas
            )

    }

}
