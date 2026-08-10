package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.EstabelecimentoGasto

data class EstablishmentListState(
    val estabelecimentos: List<EstabelecimentoGasto> = emptyList(),
    val nameFilter: String = "",
    val isLoading: Boolean = true
)
