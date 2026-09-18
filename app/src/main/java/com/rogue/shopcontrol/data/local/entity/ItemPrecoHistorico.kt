package com.rogue.shopcontrol.data.local.entity

data class ItemPrecoHistorico(
    val compraId: Long,
    val produtoId: Long,
    val dataCompra: String?,
    val valorUnitario: Double
)
