package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.AssignCategoriaToProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetHistoricoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoGastoByIdUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val produtoId: Long,
    getProdutoGastoById: GetProdutoGastoByIdUseCase,
    getHistoricoProduto: GetHistoricoProdutoUseCase,
    getCategorias: GetCategoriasUseCase,
    private val assignCategoriaToProduto: AssignCategoriaToProdutoUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            ProductDetailState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getProdutoGastoById(produtoId).collect { produto ->

                _state.value =
                    _state.value.copy(
                        produto = produto,
                        isLoading = false
                    )

            }

        }

        viewModelScope.launch {

            getHistoricoProduto(produtoId).collect { historico ->

                _state.value =
                    _state.value.copy(
                        historico = historico
                    )

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


    fun onAddCategoryClick() {

        _state.value =
            _state.value.copy(
                showCategoryPicker = true
            )

    }


    fun onCategoryPickerDismiss() {

        _state.value =
            _state.value.copy(
                showCategoryPicker = false
            )

    }


    fun onCategorySelected(categoriaId: Long) {

        viewModelScope.launch {

            assignCategoriaToProduto(produtoId, categoriaId)

            _state.value =
                _state.value.copy(
                    showCategoryPicker = false
                )

        }

    }

}
