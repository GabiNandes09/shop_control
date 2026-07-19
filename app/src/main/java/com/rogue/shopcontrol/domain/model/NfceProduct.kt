package com.rogue.shopcontrol.domain.model

data class NfceProduct(
    val codigo: String?,
    val nome: String,
    val quantidade: Double,
    val unidade: String?,
    val valorUnitario: Double,
    val valorTotal: Double
)