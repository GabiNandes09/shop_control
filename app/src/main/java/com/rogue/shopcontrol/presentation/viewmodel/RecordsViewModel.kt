package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.RecordsState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

class RecordsViewModel(
    getCompras: GetComprasUseCase
) : ViewModel() {


    private var comprasOriginais: List<CompraCompleta> = emptyList()

    private val _state =
        MutableStateFlow(
            RecordsState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getCompras().collect { compras ->

                comprasOriginais = compras

                aplicarFiltroMes()

            }

        }

    }


    fun onPreviousMonth() {

        _state.value =
            _state.value.copy(
                selectedMonth = _state.value.selectedMonth.minusMonths(1)
            )

        aplicarFiltroMes()

    }


    fun onNextMonth() {

        _state.value =
            _state.value.copy(
                selectedMonth = _state.value.selectedMonth.plusMonths(1)
            )

        aplicarFiltroMes()

    }


    private fun aplicarFiltroMes() {

        val mes = _state.value.selectedMonth

        val filtradas =
            comprasOriginais.filter { compraCompleta ->

                val data =
                    parseDataCompra(compraCompleta.compra.dataCompra)

                data != null && YearMonth.from(data) == mes

            }

        _state.value =
            _state.value.copy(
                compras = filtradas,
                isLoading = false
            )

    }

}
