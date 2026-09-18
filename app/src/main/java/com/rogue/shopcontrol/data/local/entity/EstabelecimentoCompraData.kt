package com.rogue.shopcontrol.data.local.entity

data class EstabelecimentoCompraData(
    val dataCompra: String?,
    val dataCompetencia: String?,
    val valorTotal: Double,
    val tipo: TipoCompra
) {

    val dataParaFiltro: String?
        get() = if (tipo == TipoCompra.PARCELADA) dataCompetencia else dataCompra

}
