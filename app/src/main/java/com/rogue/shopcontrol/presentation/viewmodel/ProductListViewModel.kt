package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoItensComDataUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProdutoSortOption
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductListState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductListViewModel(
    getProdutoItensComData: GetProdutoItensComDataUseCase,
    getCategorias: GetCategoriasUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase
) : ViewModel() {


    private var itensOriginais: List<ProdutoItemComData> = emptyList()

    private val _state =
        MutableStateFlow(
            ProductListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            _state.value =
                _state.value.copy(dateRange = getHomeDateRange().first())

            aplicarFiltros()

        }

        viewModelScope.launch {

            getProdutoItensComData().collect { itens ->

                itensOriginais = itens

                aplicarFiltros()

            }

        }

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                _state.value =
                    _state.value.copy(
                        categorias = categorias
                    )

            }

        }

    }


    fun onSortOptionSelected(option: ProdutoSortOption) {

        _state.value =
            _state.value.copy(
                sortOption = option
            )

        aplicarFiltros()

    }


    fun onNameFilterChanged(query: String) {

        _state.value =
            _state.value.copy(
                nameFilter = query
            )

        aplicarFiltros()

    }


    fun onCategoryFilterSelected(categoriaId: Long?) {

        _state.value =
            _state.value.copy(
                selectedCategoryId = categoriaId
            )

        aplicarFiltros()

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltros()

    }


    private fun aplicarFiltros() {

        val range = _state.value.dateRange
        val nomeFiltro = _state.value.nameFilter
        val categoriaFiltro = _state.value.selectedCategoryId

        val agregados =
            itensOriginais
                .filter { item ->

                    val data =
                        parseDataCompra(item.dataCompra)

                    data != null && data in range

                }
                .groupBy { it.produtoId to it.nomeProduto }
                .map { (chave, itens) ->

                    val primeiro = itens.first()

                    ProdutoGasto(
                        id = chave.first,
                        nome = chave.second,
                        valorTotalGasto = itens.sumOf { it.valorTotal },
                        quantidadeTotal = itens.sumOf { it.quantidade },
                        categoriaId = primeiro.categoriaId,
                        categoriaNome = primeiro.categoriaNome,
                        apelido = primeiro.apelidoProduto,
                        codigoBarras = primeiro.codigoBarras
                    )

                }
                .filter { produto ->

                    val passaNome =
                        nomeFiltro.isBlank() ||
                            produto.nome.contains(
                                nomeFiltro,
                                ignoreCase = true
                            ) ||
                            produto.apelido?.contains(
                                nomeFiltro,
                                ignoreCase = true
                            ) == true

                    val passaCategoria =
                        categoriaFiltro == null ||
                            produto.categoriaId == categoriaFiltro

                    passaNome && passaCategoria

                }

        val ordenados =
            when (_state.value.sortOption) {

                ProdutoSortOption.VALOR_GASTO ->
                    agregados.sortedByDescending { it.valorTotalGasto }

                ProdutoSortOption.QUANTIDADE ->
                    agregados.sortedByDescending { it.quantidadeTotal }

            }

        _state.value =
            _state.value.copy(
                produtos = ordenados,
                isLoading = false
            )

    }

}
