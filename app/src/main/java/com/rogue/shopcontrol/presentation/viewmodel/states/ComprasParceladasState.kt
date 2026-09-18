package com.rogue.shopcontrol.presentation.viewmodel.states

data class SerieParcelada(
    val compraIdAtual: Long,
    val nome: String,
    val categoriaNome: String?,
    val valorParcela: Double,
    val parcelaAtualNumero: Int,
    val totalParcelas: Int,
    val concluida: Boolean
)

data class ComprasParceladasState(
    val ativas: List<SerieParcelada> = emptyList(),
    val concluidas: List<SerieParcelada> = emptyList(),
    val mostrarConcluidas: Boolean = false,
    val isLoading: Boolean = true
) {

    val valorMensalComprometido: Double
        get() = ativas.sumOf { it.valorParcela }

}
