package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.domain.usecase.GetProdutosGastoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProdutoSortOption
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val getProdutosGasto: GetProdutosGastoUseCase
) : ViewModel() {


    private var produtosOriginais: List<ProdutoGasto> = emptyList()

    private val _state =
        MutableStateFlow(
            ProductListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getProdutosGasto().collect { produtos ->

                produtosOriginais = produtos

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


    private fun aplicarFiltros() {

        val filtro = _state.value.nameFilter

        val filtrados =
            produtosOriginais.filter { produto ->

                filtro.isBlank() ||
                    produto.nome.contains(
                        filtro,
                        ignoreCase = true
                    )

            }

        val ordenados =
            when (_state.value.sortOption) {

                ProdutoSortOption.VALOR_GASTO ->
                    filtrados.sortedByDescending { it.valorTotalGasto }

                ProdutoSortOption.QUANTIDADE ->
                    filtrados.sortedByDescending { it.quantidadeTotal }

            }

        _state.value =
            _state.value.copy(
                produtos = ordenados,
                isLoading = false
            )

    }

}
