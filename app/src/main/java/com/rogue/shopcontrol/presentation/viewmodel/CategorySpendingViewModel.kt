package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.GetCategoriasGastoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.CategorySpendingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategorySpendingViewModel(
    getCategoriasGasto: GetCategoriasGastoUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            CategorySpendingState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCategoriasGasto().collect { categorias ->

                _state.value = CategorySpendingState(
                    categorias = categorias,
                    isLoading = false
                )

            }

        }

    }

}
