package com.rogue.shopcontrol.presentation.viewmodel.states

data class ScannerState(
    val isScanning: Boolean = true,
    val error: String? = null,
    val savedCompraId: Long? = null
)