package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.domain.usecase.AddCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCategoriaUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.CategoryManagementState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryManagementViewModel(
    getCategorias: GetCategoriasUseCase,
    private val addCategoria: AddCategoriaUseCase,
    private val deleteCategoria: DeleteCategoriaUseCase,
    private val updateCategoria: UpdateCategoriaUseCase
) : ViewModel() {


    private var categoriasOriginais: List<CategoriaEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            CategoryManagementState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCategorias().collect { categorias ->

                categoriasOriginais = categorias

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


    fun onEditClick(categoria: CategoriaEntity) {

        _state.value =
            _state.value.copy(
                editingCategoria = categoria,
                editCategoryName = categoria.nome,
                errorRes = null
            )

    }


    fun onEditCategoryNameChanged(nome: String) {

        _state.value =
            _state.value.copy(
                editCategoryName = nome
            )

    }


    fun onSaveEdit() {

        val categoria =
            _state.value.editingCategoria
                ?: return

        viewModelScope.launch {

            val sucesso =
                updateCategoria(
                    categoria.id,
                    _state.value.editCategoryName
                )

            _state.value =
                if (sucesso) {

                    _state.value.copy(
                        editingCategoria = null,
                        errorRes = null
                    )

                } else {

                    _state.value.copy(
                        errorRes = R.string.category_add_error
                    )

                }

        }

    }


    fun onEditDismiss() {

        _state.value =
            _state.value.copy(
                editingCategoria = null
            )

    }


    private fun aplicarFiltro() {

        val filtro = _state.value.nameFilter

        val filtradas =
            categoriasOriginais.filter { categoria ->

                filtro.isBlank() ||
                    categoria.nome.contains(filtro, ignoreCase = true)

            }

        _state.value =
            _state.value.copy(
                categorias = filtradas,
                isLoading = false
            )

    }

}
