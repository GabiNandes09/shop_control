package com.rogue.shopcontrol.presentation.viewmodel.states

import androidx.annotation.StringRes
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity

data class CategoryManagementState(
    val categorias: List<CategoriaEntity> = emptyList(),
    val todasCategorias: List<CategoriaEntity> = emptyList(),
    val newCategoryName: String = "",
    val nameFilter: String = "",
    val editingCategoria: CategoriaEntity? = null,
    val editCategoryName: String = "",
    @param:StringRes val errorRes: Int? = null,
    val isLoading: Boolean = true,

    val showAddDialog: Boolean = false,

    val showGrupoPicker: Boolean = false,
    val categoriaParaGrupo: CategoriaEntity? = null,

    val showLinkProdutosDialog: Boolean = false,
    val categoriaParaLink: CategoriaEntity? = null,
    val todosProdutos: List<ProdutoEntity> = emptyList(),
    val produtosSelecionados: Set<Long> = emptySet(),
    val produtoSearchQuery: String = ""
) {

    fun grupoNomeDe(categoria: CategoriaEntity): String? =
        todasCategorias.firstOrNull { it.id == categoria.grupoId }?.nome

}
