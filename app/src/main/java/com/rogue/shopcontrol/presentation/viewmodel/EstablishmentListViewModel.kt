package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentosGastoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.EstablishmentListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstablishmentListViewModel(
    getEstabelecimentosGasto: GetEstabelecimentosGastoUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            EstablishmentListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getEstabelecimentosGasto().collect { estabelecimentos ->

                _state.value = EstablishmentListState(
                    estabelecimentos = estabelecimentos,
                    isLoading = false
                )

            }

        }

    }

}
