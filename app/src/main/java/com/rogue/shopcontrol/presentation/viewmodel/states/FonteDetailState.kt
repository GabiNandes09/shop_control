package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.domain.model.DateRange

data class FonteDetailState(
    val fonte: FonteRendaEntity? = null,
    val lancamentos: List<RendaComDados> = emptyList(),
    val dateRange: DateRange = DateRange.currentMonth(),
    val isLoading: Boolean = true
) {

    val totalRecebido: Double
        get() = lancamentos.sumOf { it.valor }

}
