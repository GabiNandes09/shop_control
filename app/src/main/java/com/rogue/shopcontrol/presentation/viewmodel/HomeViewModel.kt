package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetMonthlySpendingUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    getMonthlySpending: GetMonthlySpendingUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            HomeState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getMonthlySpending().collect { gastoMensal ->

                _state.value = HomeState(
                    gastoMensal = gastoMensal
                )

            }

        }

    }

}
