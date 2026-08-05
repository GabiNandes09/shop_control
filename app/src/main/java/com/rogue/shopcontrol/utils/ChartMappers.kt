package com.rogue.shopcontrol.utils

import com.rogue.shopcontrol.domain.model.MonthlySpending
import com.rogue.shopcontrol.presentation.model.ChartEntry
import java.time.format.DateTimeFormatter
import java.util.Locale

private val MES_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM/yy", Locale.forLanguageTag("pt-BR"))

fun MonthlySpending.toChartEntry(): ChartEntry =
    ChartEntry(
        label = MES_FORMATTER.format(yearMonth),
        value = total,
        displayValue = formatCurrency(total)
    )
