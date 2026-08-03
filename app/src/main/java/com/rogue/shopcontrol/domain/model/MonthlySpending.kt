package com.rogue.shopcontrol.domain.model

import java.time.YearMonth

data class MonthlySpending(
    val yearMonth: YearMonth,
    val total: Double
)
