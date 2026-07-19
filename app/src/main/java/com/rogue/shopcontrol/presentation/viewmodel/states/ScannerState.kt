package com.rogue.shopcontrol.presentation.viewmodel.states

data class ScannerState(
    val isScanning: Boolean = true,
    val url: String? = null,
    val error: String? = null
)