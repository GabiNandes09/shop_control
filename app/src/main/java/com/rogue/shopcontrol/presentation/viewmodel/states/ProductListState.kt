package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import java.time.YearMonth

data class ProductListState(
    val produtos: List<ProdutoGasto> = emptyList(),
    val sortOption: ProdutoSortOption = ProdutoSortOption.VALOR_GASTO,
    val nameFilter: String = "",
    val selectedMonth: YearMonth = YearMonth.now(),
    val categorias: List<CategoriaEntity> = emptyList(),
    val selectedCategoryId: Long? = null,
    val isLoading: Boolean = true
)
