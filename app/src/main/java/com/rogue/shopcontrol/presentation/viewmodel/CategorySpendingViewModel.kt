package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CategoriaGasto
import com.rogue.shopcontrol.data.local.entity.CompraComData
import com.rogue.shopcontrol.data.local.entity.ProdutoItemComData
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.GetComprasComDataUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoItensComDataUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.CategorySpendingMode
import com.rogue.shopcontrol.presentation.viewmodel.states.CategorySpendingState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CategorySpendingViewModel(
    getProdutoItensComData: GetProdutoItensComDataUseCase,
    getComprasComData: GetComprasComDataUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase
) : ViewModel() {


    private var itensOriginais: List<ProdutoItemComData> = emptyList()
    private var comprasOriginais: List<CompraComData> = emptyList()

    private val _state =
        MutableStateFlow(
            CategorySpendingState()
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

            getProdutoItensComData().collect { itens ->

                itensOriginais = itens

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getComprasComData().collect { compras ->

                comprasOriginais = compras

                aplicarFiltro()

            }

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltro()

    }


    fun onModoChanged(modo: CategorySpendingMode) {

        _state.value =
            _state.value.copy(modo = modo)

    }


    private fun aplicarFiltro() {

        val range = _state.value.dateRange

        val categoriasPorProduto =
            itensOriginais
                .filter { item ->

                    val data =
                        parseDataCompra(item.dataCompra)

                    data != null && data in range

                }
                .groupBy { it.categoriaNome }
                .map { (nome, itens) ->

                    CategoriaGasto(
                        nome = nome,
                        valorTotalGasto = itens.sumOf { it.valorTotal }
                    )

                }
                .sortedByDescending { it.valorTotalGasto }

        val categoriasPorCompra =
            comprasOriginais
                .filter { compra ->

                    val data =
                        parseDataCompra(compra.dataParaFiltro)

                    data != null && data in range

                }
                .groupBy { it.categoriaNome }
                .map { (nome, compras) ->

                    CategoriaGasto(
                        nome = nome,
                        valorTotalGasto = compras.sumOf { it.valorTotal }
                    )

                }
                .sortedByDescending { it.valorTotalGasto }

        _state.value =
            _state.value.copy(
                categoriasPorCompra = categoriasPorCompra,
                categoriasPorProduto = categoriasPorProduto,
                isLoading = false
            )

    }

}
