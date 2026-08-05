package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingHistoryUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.SpendingComparisonState
import com.rogue.shopcontrol.utils.toChartEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

}
