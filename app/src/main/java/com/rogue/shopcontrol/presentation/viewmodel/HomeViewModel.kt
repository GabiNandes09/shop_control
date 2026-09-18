package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.data.local.entity.CompraComData
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.GetARecebimentosComDadosUseCase
import com.rogue.shopcontrol.domain.usecase.GetComprasComDataUseCase
import com.rogue.shopcontrol.domain.usecase.GetHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaComDadosUseCase
import com.rogue.shopcontrol.domain.usecase.SaveHomeDateRangeUseCase
import com.rogue.shopcontrol.domain.usecase.VerificarEEstenderSeriesARecebimentoUseCase
import com.rogue.shopcontrol.domain.usecase.VerificarEEstenderSeriesFixaUseCase
import com.rogue.shopcontrol.domain.usecase.VerificarEEstenderSeriesRendaUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.HomeState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(
    getHomeDateRange: GetHomeDateRangeUseCase,
    private val saveHomeDateRange: SaveHomeDateRangeUseCase,
    verificarEEstenderSeriesRenda: VerificarEEstenderSeriesRendaUseCase,
    verificarEEstenderSeriesFixa: VerificarEEstenderSeriesFixaUseCase,
    getComprasComData: GetComprasComDataUseCase,
    getRendaComDados: GetRendaComDadosUseCase,
    verificarEEstenderSeriesARecebimento: VerificarEEstenderSeriesARecebimentoUseCase,
    getARecebimentosComDados: GetARecebimentosComDadosUseCase
) : ViewModel() {


    private var comprasOriginais: List<CompraComData> = emptyList()
    private var rendaOriginais: List<RendaComDados> = emptyList()
    private var arecebimentosOriginais: List<ARecebimentoComDados> = emptyList()

    private val _state =
        MutableStateFlow(
            HomeState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {
            verificarEEstenderSeriesRenda()
        }

        viewModelScope.launch {
            verificarEEstenderSeriesFixa()
        }

        viewModelScope.launch {
            verificarEEstenderSeriesARecebimento()
        }

        viewModelScope.launch {

            getComprasComData().collect { compras ->

                comprasOriginais = compras

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getRendaComDados().collect { rendas ->

                rendaOriginais = rendas

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getARecebimentosComDados().collect { arecebimentos ->

                arecebimentosOriginais = arecebimentos

                aplicarFiltro()

            }

        }

        viewModelScope.launch {

            getHomeDateRange().collect { range ->

                _state.value =
                    _state.value.copy(dateRange = range)

                aplicarFiltro()

            }

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        viewModelScope.launch {
            saveHomeDateRange(dateRange)
        }

    }


    fun onShowAlertasDialog() {

        _state.value =
            _state.value.copy(showAlertasDialog = true)

    }


    fun onDismissAlertasDialog() {

        _state.value =
            _state.value.copy(showAlertasDialog = false)

    }


    private fun aplicarFiltro() {

        val range = _state.value.dateRange

        val valorGasto =
            comprasOriginais
                .filter { compra ->

                    val data =
                        parseDataCompra(compra.dataParaFiltro)

                    data != null && data in range

                }
                .sumOf { it.valorTotal }

        val valorRenda =
            rendaOriginais
                .filter { renda ->

                    val data =
                        parseDataCompra(renda.data)

                    data != null && data in range

                }
                .sumOf { it.valor }

        val hoje = LocalDate.now()

        val alertas =
            arecebimentosOriginais.filter { arecebimento ->

                !arecebimento.pago &&
                    parseDataCompra(arecebimento.dataPrevista)?.isBefore(hoje) == true

            }

        _state.value =
            _state.value.copy(
                valorGasto = valorGasto,
                valorRenda = valorRenda,
                alertasARecebimento = alertas,
                isLoading = false
            )

    }

}
