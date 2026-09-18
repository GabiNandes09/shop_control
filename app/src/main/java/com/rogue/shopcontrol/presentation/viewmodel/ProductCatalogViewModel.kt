package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductCatalogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductCatalogViewModel(
    getAllProdutos: GetAllProdutosUseCase
) : ViewModel() {


    private var produtosOriginais: List<ProdutoEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            ProductCatalogState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getAllProdutos().collect { produtos ->

                produtosOriginais = produtos

                aplicarFiltro()

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


    private fun aplicarFiltro() {

        val filtro = _state.value.nameFilter

        val filtrados =
            produtosOriginais.filter { produto ->

                filtro.isBlank() ||
                    produto.nome.contains(filtro, ignoreCase = true) ||
                    produto.apelido?.contains(filtro, ignoreCase = true) == true

            }

        _state.value =
            _state.value.copy(
                produtos = filtrados,
                isLoading = false
            )

    }

}
