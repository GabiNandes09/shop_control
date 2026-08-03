package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.ProdutoGasto

data class ProductListState(
    val produtos: List<ProdutoGasto> = emptyList(),
    val sortOption: ProdutoSortOption = ProdutoSortOption.VALOR_GASTO,
    val nameFilter: String = "",
    val isLoading: Boolean = true
)
