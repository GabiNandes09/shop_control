package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.domain.usecase.AddCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.DeleteCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.LinkProdutosToCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCategoriaUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateGrupoCategoriaUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.CategoryManagementState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryManagementViewModel(
    getCategorias: GetCategoriasUseCase,
    private val addCategoria: AddCategoriaUseCase,
    private val deleteCategoria: DeleteCategoriaUseCase,
    private val updateCategoria: UpdateCategoriaUseCase,
    private val updateGrupoCategoria: UpdateGrupoCategoriaUseCase,
    getAllProdutos: GetAllProdutosUseCase,
    private val linkProdutosToCategoria: LinkProdutosToCategoriaUseCase
) : ViewModel() {


    private var categoriasOriginais: List<CategoriaEntity> = emptyList()
    private var todosProdutosOriginais: List<ProdutoEntity> = emptyList()

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

        viewModelScope.launch {

            getAllProdutos().collect { produtos ->

                todosProdutosOriginais = produtos

                _state.value =
                    _state.value.copy(todosProdutos = produtos)

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


    fun onShowAddDialog() {

        _state.value =
            _state.value.copy(
                showAddDialog = true,
                newCategoryName = "",
                errorRes = null
            )

    }


    fun onDismissAddDialog() {

        _state.value =
            _state.value.copy(showAddDialog = false)

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
                        errorRes = null,
                        showAddDialog = false
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
                deleteCategoria(categoriaId)

            if (!sucesso) {

                _state.value =
                    _state.value.copy(
                        errorRes = R.string.categoria_produto_em_uso_error
                    )

            }

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


    fun onShowGrupoPicker(categoria: CategoriaEntity) {

        _state.value =
            _state.value.copy(
                showGrupoPicker = true,
                categoriaParaGrupo = categoria
            )

    }


    fun onDismissGrupoPicker() {

        _state.value =
            _state.value.copy(
                showGrupoPicker = false,
                categoriaParaGrupo = null
            )

    }


    fun onGrupoSelected(grupoId: Long) {

        val categoria =
            _state.value.categoriaParaGrupo ?: return

        viewModelScope.launch {

            updateGrupoCategoria(categoria.id, grupoId)

            _state.value =
                _state.value.copy(
                    showGrupoPicker = false,
                    categoriaParaGrupo = null
                )

        }

    }


    fun onRemoveGrupo(categoria: CategoriaEntity) {

        viewModelScope.launch {
            updateGrupoCategoria(categoria.id, null)
        }

    }


    fun onShowLinkProdutosDialog(categoria: CategoriaEntity) {

        _state.value =
            _state.value.copy(
                showLinkProdutosDialog = true,
                categoriaParaLink = categoria,
                produtosSelecionados = todosProdutosOriginais
                    .filter { it.categoriaId == categoria.id }
                    .map { it.id }
                    .toSet(),
                produtoSearchQuery = ""
            )

    }


    fun onDismissLinkProdutosDialog() {

        _state.value =
            _state.value.copy(
                showLinkProdutosDialog = false,
                categoriaParaLink = null
            )

    }


    fun onProdutoSearchChanged(query: String) {

        _state.value =
            _state.value.copy(produtoSearchQuery = query)

    }


    fun onToggleProdutoSelecionado(produtoId: Long) {

        val atual = _state.value.produtosSelecionados

        _state.value =
            _state.value.copy(
                produtosSelecionados = if (produtoId in atual) {
                    atual - produtoId
                } else {
                    atual + produtoId
                }
            )

    }


    fun onSaveLinkProdutos() {

        val categoria =
            _state.value.categoriaParaLink ?: return

        val selecionados =
            _state.value.produtosSelecionados

        val jaVinculados =
            todosProdutosOriginais
                .filter { it.categoriaId == categoria.id }
                .map { it.id }
                .toSet()

        val paraVincular =
            (selecionados - jaVinculados).toList()

        val paraDesvincular =
            (jaVinculados - selecionados).toList()

        viewModelScope.launch {

            linkProdutosToCategoria(categoria.id, paraVincular, paraDesvincular)

            _state.value =
                _state.value.copy(
                    showLinkProdutosDialog = false,
                    categoriaParaLink = null
                )

        }

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
                todasCategorias = categoriasOriginais,
                isLoading = false
            )

    }

}
