package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.presentation.model.ChartEntry
import java.time.YearMonth

data class EstablishmentDetailState(
    val estabelecimento: EstabelecimentoEntity? = null,
    val chartEntries: List<ChartEntry> = emptyList(),
    val compras: List<CompraCompleta> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.now(),
    val showEditDialog: Boolean = false,
    val editApelidoText: String = "",
    val isLoading: Boolean = true
) {

    val totalGasto: Double
        get() = chartEntries.sumOf { it.value }

}
