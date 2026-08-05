package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.ProdutoEntity

data class ProductCatalogState(
    val produtos: List<ProdutoEntity> = emptyList(),
    val isLoading: Boolean = true
)
