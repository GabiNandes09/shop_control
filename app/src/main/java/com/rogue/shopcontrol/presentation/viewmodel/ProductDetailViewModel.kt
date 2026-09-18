package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.AddCategoriaERetornarIdUseCase
import com.rogue.shopcontrol.domain.usecase.AssignCategoriaToProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetAllProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.GetCategoriasUseCase
import com.rogue.shopcontrol.domain.usecase.GetHistoricoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.GetProdutoGastoByIdUseCase
import com.rogue.shopcontrol.domain.usecase.MergeProdutosUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateApelidoProdutoUseCase
import com.rogue.shopcontrol.domain.usecase.UpdateCodigoBarrasProdutoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.ProductDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val produtoId: Long,
    getProdutoGastoById: GetProdutoGastoByIdUseCase,
    getHistoricoProduto: GetHistoricoProdutoUseCase,
    getCategorias: GetCategoriasUseCase,
    private val assignCategoriaToProduto: AssignCategoriaToProdutoUseCase,
    getAllProdutos: GetAllProdutosUseCase,
    private val updateCodigoBarrasProduto: UpdateCodigoBarrasProdutoUseCase,
    private val updateApelidoProduto: UpdateApelidoProdutoUseCase,
    private val mergeProdutos: MergeProdutosUseCase,
    private val addCategoriaERetornarId: AddCategoriaERetornarIdUseCase
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

        viewModelScope.launch {

            getAllProdutos().collect { produtos ->

                _state.value =
                    _state.value.copy(
                        todosProdutos = produtos.filter { it.id != produtoId }
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


    fun onCreateCategory(nome: String) {

        viewModelScope.launch {

            val categoriaId = addCategoriaERetornarId(nome)

            assignCategoriaToProduto(produtoId, categoriaId)

            _state.value =
                _state.value.copy(
                    showCategoryPicker = false
                )

        }

    }


    fun onEditEanClick() {

        _state.value =
            _state.value.copy(
                showEanDialog = true,
                eanInput = _state.value.produto?.codigoBarras ?: ""
            )

    }


    fun onEanInputChanged(texto: String) {

        _state.value =
            _state.value.copy(eanInput = texto)

    }


    fun onSaveEan() {

        viewModelScope.launch {

            updateCodigoBarrasProduto(
                produtoId,
                _state.value.eanInput.trim().ifBlank { null }
            )

            _state.value =
                _state.value.copy(showEanDialog = false)

        }

    }


    fun onEanDialogDismiss() {

        _state.value =
            _state.value.copy(showEanDialog = false)

    }


    fun onEditApelidoClick() {

        _state.value =
            _state.value.copy(
                showApelidoDialog = true,
                apelidoInput = _state.value.produto?.apelido ?: ""
            )

    }


    fun onApelidoInputChanged(texto: String) {

        _state.value =
            _state.value.copy(apelidoInput = texto)

    }


    fun onSaveApelido() {

        viewModelScope.launch {

            updateApelidoProduto(
                produtoId,
                _state.value.apelidoInput.trim().ifBlank { null }
            )

            _state.value =
                _state.value.copy(showApelidoDialog = false)

        }

    }


    fun onApelidoDialogDismiss() {

        _state.value =
            _state.value.copy(showApelidoDialog = false)

    }


    fun onShowMergePicker() {

        _state.value =
            _state.value.copy(showMergePicker = true)

    }


    fun onDismissMergePicker() {

        _state.value =
            _state.value.copy(showMergePicker = false)

    }


    fun onMergeTargetSelected(destinoId: Long) {

        _state.value =
            _state.value.copy(
                showMergePicker = false,
                showMergeConfirm = true,
                pendingMergeTargetId = destinoId
            )

    }


    fun onDismissMergeConfirm() {

        _state.value =
            _state.value.copy(
                showMergeConfirm = false,
                pendingMergeTargetId = null
            )

    }


    fun onConfirmMerge() {

        val destinoId = _state.value.pendingMergeTargetId ?: return

        viewModelScope.launch {

            mergeProdutos(produtoId, destinoId)

            _state.value =
                _state.value.copy(
                    showMergeConfirm = false,
                    isMerged = true
                )

        }

    }

}
