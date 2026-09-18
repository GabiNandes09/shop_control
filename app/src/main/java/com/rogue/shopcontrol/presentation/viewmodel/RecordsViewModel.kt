package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.GetComprasUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.RecordsState
import com.rogue.shopcontrol.presentation.viewmodel.states.TipoFiltroCompra
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecordsViewModel(
    getCompras: GetComprasUseCase,
    getHomeDateRange: GetHomeDateRangeUseCase
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

            _state.value =
                _state.value.copy(dateRange = getHomeDateRange().first())

            aplicarFiltro()

        }

        viewModelScope.launch {

            getCompras().collect { compras ->

                comprasOriginais = compras

                aplicarFiltro()

            }

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltro()

    }


    fun onTipoFiltroChanged(tipoFiltro: TipoFiltroCompra) {

        _state.value =
            _state.value.copy(tipoFiltro = tipoFiltro)

        aplicarFiltro()

    }


    private fun aplicarFiltro() {

        val range = _state.value.dateRange
        val tipoFiltro = _state.value.tipoFiltro

        val filtradas =
            comprasOriginais.filter { compraCompleta ->

                val data =
                    parseDataCompra(compraCompleta.compra.dataParaFiltro)

                val passaData = data != null && data in range

                val passaTipo =
                    when (tipoFiltro) {
                        TipoFiltroCompra.TODOS -> true
                        TipoFiltroCompra.VARIAVEIS -> compraCompleta.compra.tipo == TipoCompra.VARIAVEL
                        TipoFiltroCompra.FIXAS -> compraCompleta.compra.tipo == TipoCompra.FIXA
                        TipoFiltroCompra.PARCELADAS -> compraCompleta.compra.tipo == TipoCompra.PARCELADA
                        TipoFiltroCompra.RAPIDAS -> compraCompleta.compra.tipo == TipoCompra.RAPIDA
                    }

                passaData && passaTipo

            }

        _state.value =
            _state.value.copy(
                compras = filtradas,
                isLoading = false
            )

    }

}
