package com.rogue.shopcontrol.data.local.entity

data class ProdutoItemComData(
    val produtoId: Long,
    val nomeProduto: String,
    val categoriaId: Long?,
    val categoriaNome: String?,
    val dataCompra: String?,
    val quantidade: Double,
    val valorTotal: Double
)
