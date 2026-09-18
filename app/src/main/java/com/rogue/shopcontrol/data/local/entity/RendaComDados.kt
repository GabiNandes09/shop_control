package com.rogue.shopcontrol.data.local.entity

data class RendaComDados(
    val id: Long,
    val descricao: String,
    val valor: Double,
    val data: String,
    val recorrente: Boolean,
    val origemRecorrenteId: Long?,
    val rendaCategoriaId: Long,
    val categoriaNome: String,
    val fonteRendaId: Long,
    val fonteNome: String
)
