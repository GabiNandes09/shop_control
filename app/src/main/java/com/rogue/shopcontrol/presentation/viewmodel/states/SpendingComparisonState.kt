package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.presentation.model.ChartEntry

data class SpendingComparisonState(
    val entries: List<ChartEntry> = emptyList(),
    val isLoading: Boolean = true
)
