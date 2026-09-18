package com.rogue.shopcontrol.data.local.entity

data class CompraComData(
    val compraId: Long,
    val estabelecimentoId: Long,
    val dataCompra: String?,
    val dataCompetencia: String?,
    val valorTotal: Double,
    val tipo: TipoCompra,
    val categoriaId: Long?,
    val categoriaNome: String?
) {

    val dataParaFiltro: String?
        get() = if (tipo == TipoCompra.PARCELADA) dataCompetencia else dataCompra

}
