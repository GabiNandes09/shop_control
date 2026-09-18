package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaGasto
import com.rogue.shopcontrol.domain.model.DateRange

enum class CategorySpendingMode {
    POR_COMPRA,
    POR_PRODUTO
}

data class CategorySpendingState(
    val modo: CategorySpendingMode = CategorySpendingMode.POR_COMPRA,
    val categoriasPorCompra: List<CategoriaGasto> = emptyList(),
    val categoriasPorProduto: List<CategoriaGasto> = emptyList(),
    val dateRange: DateRange = DateRange.currentMonth(),
    val isLoading: Boolean = true
) {

    val categorias: List<CategoriaGasto>
        get() = if (modo == CategorySpendingMode.POR_COMPRA) categoriasPorCompra else categoriasPorProduto

}
