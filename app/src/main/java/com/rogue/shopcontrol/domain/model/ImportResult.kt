package com.rogue.shopcontrol.domain.model

data class ImportResult(
    val estabelecimentos: Int,
    val produtos: Int,
    val categorias: Int,
    val compras: Int,
    val itensCompra: Int,
    val rendaCategorias: Int,
    val fontesRenda: Int,
    val rendas: Int
)
