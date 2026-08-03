package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.ProdutoGasto

data class HomeState(
    val gastoMensal: Double = 0.0,
    val topProdutos: List<ProdutoGasto> = emptyList()
)
