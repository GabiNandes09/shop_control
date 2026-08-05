package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.domain.usecase.AddCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.CategoryManagementState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryManagementViewModel(
    getCategorias: GetCategoriasUseCase,
    private val addCategoria: AddCategoriaUseCase,
    private val deleteCategoria: DeleteCategoriaUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            CategoryManagementState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                _state.value =
                    _state.value.copy(
                        categorias = categorias,
                        isLoading = false
                    )

            }

        }

    }


    fun onNewCategoryNameChanged(nome: String) {

        _state.value =
            _state.value.copy(
                newCategoryName = nome,
                errorRes = null
            )

    }


    fun onAddCategory() {

        viewModelScope.launch {

            val sucesso =
                addCategoria(_state.value.newCategoryName)

            _state.value =
                if (sucesso) {

                    _state.value.copy(
                        newCategoryName = "",
                        errorRes = null
                    )

                } else {

                    _state.value.copy(
                        errorRes = R.string.category_add_error
                    )

                }

        }

    }


    fun onDeleteCategory(categoriaId: Long) {

        viewModelScope.launch {

            deleteCategoria(categoriaId)

        }

    }

}
