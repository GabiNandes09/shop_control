package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import java.time.YearMonth

data class RecordsState(
    val compras: List<CompraCompleta> = emptyList(),
    val selectedMonth: YearMonth = YearMonth.now(),
    val isLoading: Boolean = true
)
