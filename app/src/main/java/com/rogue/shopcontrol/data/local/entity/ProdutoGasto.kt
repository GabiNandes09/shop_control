package com.rogue.shopcontrol.data.local.entity

data class ProdutoGasto(
    val id: Long,
    val nome: String,
    val valorTotalGasto: Double,
    val quantidadeTotal: Double,
    val categoriaId: Long? = null,
    val categoriaNome: String? = null,
    val apelido: String? = null,
    val codigoBarras: String? = null
)
