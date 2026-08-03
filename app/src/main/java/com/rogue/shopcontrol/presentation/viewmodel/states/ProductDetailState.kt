package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto

data class ProductDetailState(
    val produto: ProdutoGasto? = null,
    val historico: List<ProdutoCompraHistorico> = emptyList(),
    val isLoading: Boolean = true
)
