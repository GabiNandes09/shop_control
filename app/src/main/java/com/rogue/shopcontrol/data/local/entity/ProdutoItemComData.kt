package com.rogue.shopcontrol.data.local.entity

data class ProdutoItemComData(
    val produtoId: Long,
    val nomeProduto: String,
    val apelidoProduto: String?,
    val categoriaId: Long?,
    val categoriaNome: String?,
    val codigoBarras: String?,
    val dataCompra: String?,
    val quantidade: Double,
    val valorTotal: Double
)
