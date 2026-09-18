package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetBalancoMensalHistoricoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.BalancoState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BalancoViewModel(
    getBalancoMensalHistorico: GetBalancoMensalHistoricoUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            BalancoState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getBalancoMensalHistorico().collect { entries ->

                _state.value =
                    BalancoState(
                        entries = entries,
                        isLoading = false
                    )

            }

        }

    }

}
