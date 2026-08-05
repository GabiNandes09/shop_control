package com.rogue.shopcontrol.presentation.viewmodel.states

import androidx.annotation.StringRes
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity

data class CategoryManagementState(
    val categorias: List<CategoriaEntity> = emptyList(),
    val newCategoryName: String = "",
    @param:StringRes val errorRes: Int? = null,
    val isLoading: Boolean = true
)
