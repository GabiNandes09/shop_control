package com.rogue.shopcontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto
import com.rogue.shopcontrol.domain.usecase.GetEstabelecimentosGastoUseCase
import com.rogue.shopcontrol.presentation.viewmodel.states.EstablishmentListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EstablishmentListViewModel(
    getEstabelecimentosGasto: GetEstabelecimentosGastoUseCase
) : ViewModel() {


    private var estabelecimentosOriginais: List<EstabelecimentoGasto> = emptyList()

    private val _state =
        MutableStateFlow(
            EstablishmentListState()
        )

    val state =
        _state.asStateFlow()


    init {

        viewModelScope.launch {

            getEstabelecimentosGasto().collect { estabelecimentos ->

                estabelecimentosOriginais = estabelecimentos

                aplicarFiltro()

            }

        }

    }


    fun onNameFilterChanged(query: String) {

        _state.value =
            _state.value.copy(
                nameFilter = query
            )

        aplicarFiltro()

    }


    private fun aplicarFiltro() {

        val filtro = _state.value.nameFilter

        val filtrados =
            estabelecimentosOriginais.filter { estabelecimento ->

                filtro.isBlank() ||
                    estabelecimento.nome.contains(filtro, ignoreCase = true) ||
                    estabelecimento.apelido?.contains(filtro, ignoreCase = true) == true

            }

        _state.value =
            _state.value.copy(
                estabelecimentos = filtrados,
                isLoading = false
            )

    }

}
