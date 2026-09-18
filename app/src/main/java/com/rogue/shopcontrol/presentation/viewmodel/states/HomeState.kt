package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.domain.model.DateRange

data class HomeState(
    val valorGasto: Double = 0.0,
    val valorRenda: Double = 0.0,
    val dateRange: DateRange = DateRange.currentMonth(),
    val alertasARecebimento: List<ARecebimentoComDados> = emptyList(),
    val showAlertasDialog: Boolean = false,
    val isLoading: Boolean = true
) {

    val saldo: Double
        get() = valorRenda - valorGasto

}
