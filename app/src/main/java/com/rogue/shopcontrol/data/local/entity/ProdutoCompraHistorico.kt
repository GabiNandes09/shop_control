package com.rogue.shopcontrol.data.local.entity

data class ProdutoCompraHistorico(
    val dataCompra: String?,
    val nomeEstabelecimento: String,
    val quantidade: Double,
    val valorUnitario: Double,
    val valorTotal: Double
)
