package com.rogue.shopcontrol.presentation.viewmodel.states

import androidx.annotation.StringRes
import com.rogue.shopcontrol.data.local.entity.RendaCategoriaEntity

data class RendaCategoriaManagementState(
    val categorias: List<RendaCategoriaEntity> = emptyList(),
    val newCategoryName: String = "",
    val nameFilter: String = "",
    val editingCategoria: RendaCategoriaEntity? = null,
    val editCategoryName: String = "",
    @param:StringRes val errorRes: Int? = null,
    val isLoading: Boolean = true
)
