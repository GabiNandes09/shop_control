package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CompraCompleta

data class RecordsState(
    val compras: List<CompraCompleta> = emptyList(),
    val isLoading: Boolean = true
)
