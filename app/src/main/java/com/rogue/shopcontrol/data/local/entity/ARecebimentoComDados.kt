package com.rogue.shopcontrol.data.local.entity

data class ARecebimentoComDados(
    val id: Long,
    val descricao: String,
    val valor: Double,
    val dataPrevista: String,
    val tipo: TipoARecebimento,
    val totalParcelas: Int?,
    val origemRecorrenteId: Long?,
    val pago: Boolean,
    val dataPagamento: String?,
    val rendaGeradaId: Long?,
    val rendaCategoriaId: Long,
    val categoriaNome: String,
    val fonteRendaId: Long,
    val fonteNome: String
)
