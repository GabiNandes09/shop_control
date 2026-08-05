package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import com.rogue.shopcontrol.domain.usecase.GetProdutoItensComDataUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProdutoSortOption
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductListState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

class ProductListViewModel(
    getProdutoItensComData: GetProdutoItensComDataUseCase
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

            getProdutoItensComData().collect { itens ->

                itensOriginais = itens

                aplicarFiltros()

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


    fun onPreviousMonth() {

        _state.value =
            _state.value.copy(
                selectedMonth = _state.value.selectedMonth.minusMonths(1)
            )

        aplicarFiltros()

    }


    fun onNextMonth() {

        _state.value =
            _state.value.copy(
                selectedMonth = _state.value.selectedMonth.plusMonths(1)
            )

        aplicarFiltros()

    }


    private fun aplicarFiltros() {

        val mes = _state.value.selectedMonth
        val nomeFiltro = _state.value.nameFilter

        val agregados =
            itensOriginais
                .filter { item ->

                    val data =
                        parseDataCompra(item.dataCompra)

                    data != null && YearMonth.from(data) == mes

                }
                .groupBy { it.produtoId to it.nomeProduto }
                .map { (chave, itens) ->

                    ProdutoGasto(
                        id = chave.first,
                        nome = chave.second,
                        valorTotalGasto = itens.sumOf { it.valorTotal },
                        quantidadeTotal = itens.sumOf { it.quantidade }
                    )

                }
                .filter { produto ->

                    nomeFiltro.isBlank() ||
                        produto.nome.contains(
                            nomeFiltro,
                            ignoreCase = true
                        )

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
