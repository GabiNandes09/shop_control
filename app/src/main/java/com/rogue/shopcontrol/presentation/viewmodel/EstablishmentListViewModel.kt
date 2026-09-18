package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoCompraComData
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.AddCategoriaERetornarIdUseCase
import com.rogue.shopcontrol.domain.usecase.AddEstabelecimentoUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllEstabelecimentosUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetTodasComprasComEstabelecimentoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.EstablishmentListState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EstablishmentListViewModel(
    getAllEstabelecimentos: GetAllEstabelecimentosUseCase,
    getTodasComprasComEstabelecimento: GetTodasComprasComEstabelecimentoUseCase,
    private val addEstabelecimento: AddEstabelecimentoUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase,
    getCategorias: GetCategoriasUseCase,
    private val addCategoriaERetornarId: AddCategoriaERetornarIdUseCase
) : ViewModel() {


    private var estabelecimentosOriginais: List<EstabelecimentoEntity> = emptyList()
    private var comprasOriginais: List<EstabelecimentoCompraComData> = emptyList()

    private val _state =
        MutableStateFlow(
            EstablishmentListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(dateRange = getHomeDateRange().first())

            aplicarFiltro()

        }

        viewModelScope.launch {

            getAllEstabelecimentos().collect { lista ->

                estabelecimentosOriginais = lista

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getTodasComprasComEstabelecimento().collect { compras ->

                comprasOriginais = compras

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                _state.value =
                    _state.value.copy(categorias = categorias)

            }

        }

    }


    fun onNameFilterChanged(query: String) {

        _state.value =
            _state.value.copy(
                nameFilter = query
            )

        aplicarFiltro()

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltro()

    }


    fun onShowAddDialog() {

        _state.value =
            _state.value.copy(
                showAddDialog = true,
                newNome = "",
                newCnpj = "",
                newEndereco = "",
                newApelido = "",
                newCategoriaId = null,
                addErrorRes = null
            )

    }


    fun onShowNewCategoryPicker() {

        _state.value =
            _state.value.copy(showNewCategoryPicker = true)

    }


    fun onDismissNewCategoryPicker() {

        _state.value =
            _state.value.copy(showNewCategoryPicker = false)

    }


    fun onNewCategoriaSelecionada(categoriaId: Long) {

        _state.value =
            _state.value.copy(
                newCategoriaId = categoriaId,
                showNewCategoryPicker = false
            )

    }


    fun onCreateNewCategoria(nome: String) {

        viewModelScope.launch {

            val categoriaId = addCategoriaERetornarId(nome)

            _state.value =
                _state.value.copy(
                    newCategoriaId = categoriaId,
                    showNewCategoryPicker = false
                )

        }

    }


    fun onDismissAddDialog() {

        _state.value =
            _state.value.copy(showAddDialog = false)

    }


    fun onNewNomeChanged(texto: String) {

        _state.value =
            _state.value.copy(newNome = texto, addErrorRes = null)

    }


    fun onNewCnpjChanged(texto: String) {

        _state.value =
            _state.value.copy(newCnpj = texto)

    }


    fun onNewEnderecoChanged(texto: String) {

        _state.value =
            _state.value.copy(newEndereco = texto)

    }


    fun onNewApelidoChanged(texto: String) {

        _state.value =
            _state.value.copy(newApelido = texto)

    }


    fun onSaveNewEstabelecimento() {

        val estadoAtual = _state.value

        if (estadoAtual.newNome.isBlank()) {

            _state.value =
                estadoAtual.copy(addErrorRes = R.string.establishment_name_required_error)

            return

        }

        viewModelScope.launch {

            addEstabelecimento(
                nome = estadoAtual.newNome,
                cnpj = estadoAtual.newCnpj,
                endereco = estadoAtual.newEndereco,
                apelido = estadoAtual.newApelido,
                categoriaId = estadoAtual.newCategoriaId
            )

            _state.value =
                _state.value.copy(showAddDialog = false)

        }

    }


    private fun aplicarFiltro() {

        val filtroNome = _state.value.nameFilter
        val range = _state.value.dateRange

        val gastoPorEstabelecimento =
            comprasOriginais
                .filter { compra ->

                    val data =
                        parseDataCompra(compra.dataParaFiltro)

                    data != null && data in range

                }
                .groupBy { it.estabelecimentoId }
                .mapValues { (_, compras) ->
                    compras.sumOf { it.valorTotal }
                }

        val agregados =
            estabelecimentosOriginais
                .map { estabelecimento ->

                    EstabelecimentoGasto(
                        id = estabelecimento.id,
                        nome = estabelecimento.nome,
                        apelido = estabelecimento.apelido,
                        valorTotalGasto = gastoPorEstabelecimento[estabelecimento.id] ?: 0.0
                    )

                }
                .filter { estabelecimento ->

                    filtroNome.isBlank() ||
                        estabelecimento.nome.contains(filtroNome, ignoreCase = true) ||
                        estabelecimento.apelido?.contains(filtroNome, ignoreCase = true) == true

                }
                .sortedByDescending { it.valorTotalGasto }

        _state.value =
            _state.value.copy(
                estabelecimentos = agregados,
                isLoading = false
            )

    }

}
