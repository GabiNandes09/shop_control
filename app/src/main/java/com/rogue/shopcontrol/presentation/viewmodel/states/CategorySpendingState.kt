package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaGasto

data class CategorySpendingState(
    val categorias: List<CategoriaGasto> = emptyList(),
    val isLoading: Boolean = true
)
