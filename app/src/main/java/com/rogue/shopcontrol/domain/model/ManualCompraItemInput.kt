package com.rogue.shopcontrol.domain.model

data class ManualCompraItemInput(
    val produtoId: Long,
    val quantidade: Double,
    val valorUnitario: Double,
    val valorTotal: Double
)
