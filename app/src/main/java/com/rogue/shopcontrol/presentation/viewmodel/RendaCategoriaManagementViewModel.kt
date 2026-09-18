package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity
import com.rogue.shopcontrol.domain.usecase.AddRendaCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteRendaCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateRendaCategoriaUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.RendaCategoriaManagementState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RendaCategoriaManagementViewModel(
    getRendaCategorias: GetRendaCategoriasUseCase,
    private val addRendaCategoria: AddRendaCategoriaUseCase,
    private val deleteRendaCategoria: DeleteRendaCategoriaUseCase,
    private val updateRendaCategoria: UpdateRendaCategoriaUseCase
) : ViewModel() {


    private var categoriasOriginais: List<RendaCategoriaEntity> = emptyList()

    private val _state =
        MutableStateFlow(
            RendaCategoriaManagementState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getRendaCategorias().collect { categorias ->

                categoriasOriginais = categorias

                aplicarFiltro()

            }

        }

    }


    fun onNameFilterChanged(query: String) {

        _state.value =
            _state.value.copy(nameFilter = query)

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
                addRendaCategoria(_state.value.newCategoryName)

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

            val sucesso =
                deleteRendaCategoria(categoriaId)

            if (!sucesso) {

                _state.value =
                    _state.value.copy(
                        errorRes = R.string.renda_categoria_em_uso_error
                    )

            }

        }

    }


    fun onEditClick(categoria: RendaCategoriaEntity) {

        _state.value =
            _state.value.copy(
                editingCategoria = categoria,
                editCategoryName = categoria.nome,
                errorRes = null
            )

    }


    fun onEditCategoryNameChanged(nome: String) {

        _state.value =
            _state.value.copy(editCategoryName = nome)

    }


    fun onSaveEdit() {

        val categoria =
            _state.value.editingCategoria ?: return

        viewModelScope.launch {

            val sucesso =
                updateRendaCategoria(
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
            _state.value.copy(editingCategoria = null)

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
