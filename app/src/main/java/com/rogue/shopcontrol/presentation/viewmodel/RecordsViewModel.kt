package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.RecordsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordsViewModel(
    getCompras: GetComprasUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            RecordsState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCompras().collect { compras ->

                _state.value = RecordsState(
                    compras = compras,
                    isLoading = false
                )

            }

        }

    }

}
