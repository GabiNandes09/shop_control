package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.model.MonthlySpending
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingHistoryUseCase
import com.rogue.shopcontrol.presentation.model.ChartEntry
import com.rogue.shopcontrol.presentation.viewmodel.states.SpendingComparisonState
import com.rogue.shopcontrol.utils.formatCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

private val MES_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM/yy", Locale.forLanguageTag("pt-BR"))

class SpendingComparisonViewModel(
    getMonthlySpendingHistory: GetMonthlySpendingHistoryUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            SpendingComparisonState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getMonthlySpendingHistory().collect { historico ->

                _state.value = SpendingComparisonState(
                    entries = historico.map { it.toChartEntry() },
                    isLoading = false
                )

            }

        }

    }


    private fun MonthlySpending.toChartEntry() =
        ChartEntry(
            label = MES_FORMATTER.format(yearMonth),
            value = total,
            displayValue = formatCurrency(total)
        )

}
