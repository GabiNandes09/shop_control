package com.rogue.shopcontrol.presentation.viewmodel.states

import android.net.Uri
import com.rogue.shopcontrol.domain.model.ImportResult

data class SettingsState(
    val isExporting: Boolean = false,
    val exportedFileUri: Uri? = null,
    val exportErrorRes: Int? = null,

    val isImporting: Boolean = false,
    val showImportConfirm: Boolean = false,
    val pendingImportUri: Uri? = null,
    val importResult: ImportResult? = null,
    val importErrorRes: Int? = null
)
