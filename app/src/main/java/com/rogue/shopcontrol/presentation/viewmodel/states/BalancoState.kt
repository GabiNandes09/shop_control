package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.domain.model.BalancoMensal

data class BalancoState(
    val entries: List<BalancoMensal> = emptyList(),
    val isLoading: Boolean = true
)
