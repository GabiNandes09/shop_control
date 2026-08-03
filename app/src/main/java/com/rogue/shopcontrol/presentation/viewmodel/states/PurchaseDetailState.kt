package com.rogue.shopcontrol.presentation.viewmodel.states

import com.rogue.shopcontrol.data.local.entity.CompraCompleta

data class PurchaseDetailState(
    val compra: CompraCompleta? = null,
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false
)
