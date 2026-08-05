package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductCatalogState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductCatalogViewModel(
    getAllProdutos: GetAllProdutosUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            ProductCatalogState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getAllProdutos().collect { produtos ->

                _state.value = ProductCatalogState(
                    produtos = produtos,
                    isLoading = false
                )

            }

        }

    }

}
