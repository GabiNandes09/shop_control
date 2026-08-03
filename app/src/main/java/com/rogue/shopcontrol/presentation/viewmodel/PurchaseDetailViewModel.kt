package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.domain.usecase.DeleteCompraUseCase
import com.rogue.shopcontrol.domain.usecase.GetCompraByIdUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.PurchaseDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PurchaseDetailViewModel(
    private val compraId: Long,
    getCompraById: GetCompraByIdUseCase,
    private val deleteCompra: DeleteCompraUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            PurchaseDetailState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCompraById(compraId).collect { compra ->

                if (!_state.value.isDeleted) {

                    _state.value = PurchaseDetailState(
                        compra = compra,
                        isLoading = false
                    )

                }

            }

        }

    }


    fun delete() {

        viewModelScope.launch {

            deleteCompra(compraId)

            _state.value =
                _state.value.copy(
                    isDeleted = true
                )

        }

    }

}
