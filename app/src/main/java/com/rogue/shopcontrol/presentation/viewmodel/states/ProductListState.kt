package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.domain.model.DateRange

data class ProductListState(
    val produtos: List<ProdutoGasto> = emptyList(),
    val sortOption: ProdutoSortOption = ProdutoSortOption.VALOR_GASTO,
    val nameFilter: String = "",
    val dateRange: DateRange = DateRange.currentMonth(),
    val categorias: List<CategoriaEntity> = emptyList(),
    val selectedCategoryId: Long? = null,
    val isLoading: Boolean = true
)
