package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.domain.model.DateRange

enum class TipoFiltroCompra {
    TODOS,
    VARIAVEIS,
    FIXAS,
    PARCELADAS,
    RAPIDAS
}

data class RecordsState(
    val compras: List<CompraCompleta> = emptyList(),
    val dateRange: DateRange = DateRange.currentMonth(),
    val tipoFiltro: TipoFiltroCompra = TipoFiltroCompra.TODOS,
    val isLoading: Boolean = true
)
