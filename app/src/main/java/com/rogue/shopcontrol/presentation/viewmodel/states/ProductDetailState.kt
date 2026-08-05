package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CategoriaEntity
import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto

data class ProductDetailState(
    val produto: ProdutoGasto? = null,
    val historico: List<ProdutoCompraHistorico> = emptyList(),
    val categorias: List<CategoriaEntity> = emptyList(),
    val showCategoryPicker: Boolean = false,
    val isLoading: Boolean = true
) {

    val highestPrice: Double?
        get() = historico.maxOfOrNull { it.valorUnitario }

    val lowestPrice: Double?
        get() = historico.minOfOrNull { it.valorUnitario }

}
