package com.rogue.shopcontrol.data.local.entity

data class EstabelecimentoCompraComData(
    val estabelecimentoId: Long,
    val nome: String,
    val apelido: String?,
    val dataCompra: String?,
    val dataCompetencia: String?,
    val valorTotal: Double,
    val tipo: TipoCompra
) {

    val dataParaFiltro: String?
        get() = if (tipo == TipoCompra.PARCELADA) dataCompetencia else dataCompra

}
