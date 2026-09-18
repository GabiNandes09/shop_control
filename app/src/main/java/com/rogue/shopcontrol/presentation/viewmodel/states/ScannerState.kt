package com.rogue.shopcontrol.presentation.viewmodel.states

import androidx.annotation.StringRes

data class ScannerState(
    val isScanning: Boolean = true,
    val isLoading: Boolean = false,
    @StringRes val errorRes: Int? = null,
    val savedCompraId: Long? = null
)
