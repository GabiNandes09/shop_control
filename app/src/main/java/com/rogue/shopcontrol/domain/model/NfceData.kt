package com.rogue.shopcontrol.domain.model

data class NfceData(
    val chaveAcesso: String,
    val estabelecimento: NfceEstablishment,
    val numeroNota: String?,
    val dataEmissao: String?,
    val valorTotal: Double,
    val produtos: List<NfceProduct>
)