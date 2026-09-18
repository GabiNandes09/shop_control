package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.RendaComDados
import com.rogue.shopcontrol.domain.model.DateRange
import com.rogue.shopcontrol.domain.usecase.GetFonteRendaByIdUseCase
import com.rogue.shopcontrol.domain.usecase.GetRendaComDadosUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.FonteDetailState
import com.rogue.shopcontrol.utils.parseDataCompra
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FonteDetailViewModel(
    private val fonteId: Long,
    getFonteRendaById: GetFonteRendaByIdUseCase,
    getRendaComDados: GetRendaComDadosUseCase
) : ViewModel() {


    private var lancamentosOriginais: List<RendaComDados> = emptyList()

    private val _state =
        MutableStateFlow(
            FonteDetailState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getFonteRendaById(fonteId).collect { fonte ->

                _state.value =
                    _state.value.copy(
                        fonte = fonte,
                        isLoading = false
                    )

            }

        }

        viewModelScope.launch {

            getRendaComDados().collect { renda ->

                lancamentosOriginais =
                    renda.filter { it.fonteRendaId == fonteId }

                aplicarFiltro()

            }

        }

    }


    fun onDateRangeChanged(dateRange: DateRange) {

        _state.value =
            _state.value.copy(dateRange = dateRange)

        aplicarFiltro()

    }


    private fun aplicarFiltro() {

        val range = _state.value.dateRange

        val filtrados =
            lancamentosOriginais.filter { item ->

                val data =
                    parseDataCompra(item.data)

                data != null && data in range

            }

        _state.value =
            _state.value.copy(lancamentos = filtrados)

    }

}
