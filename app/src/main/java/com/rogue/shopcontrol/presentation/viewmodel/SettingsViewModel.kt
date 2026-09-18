package com.rogue.shopcontrol.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.domain.usecase.ExportBackupUseCase
import com.rogue.shopcontrol.domain.usecase.ImportBackupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.rogue.shopcontrol.presentation.viewmodel.states.SettingsState

class SettingsViewModel(
    private val exportBackup: ExportBackupUseCase,
    private val importBackup: ImportBackupUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            SettingsState()
        )

    val state =
        _state.asStateFlow()


    fun onExportClick() {

        if (_state.value.isExporting) {
            return
        }

        _state.value =
            _state.value.copy(isExporting = true)

        viewModelScope.launch {

            try {

                val uri = exportBackup()

                _state.value =
                    _state.value.copy(
                        isExporting = false,
                        exportedFileUri = uri
                    )

            } catch (e: Exception) {

                _state.value =
                    _state.value.copy(
                        isExporting = false,
                        exportErrorRes = R.string.export_error
                    )

            }

        }

    }


    fun onExportHandled() {

        _state.value =
            _state.value.copy(exportedFileUri = null)

    }


    fun onExportErrorShown() {

        _state.value =
            _state.value.copy(exportErrorRes = null)

    }


    fun onImportFileSelected(uri: Uri) {

        _state.value =
            _state.value.copy(
                showImportConfirm = true,
                pendingImportUri = uri
            )

    }


    fun onDismissImportConfirm() {

        _state.value =
            _state.value.copy(
                showImportConfirm = false,
                pendingImportUri = null
            )

    }


    fun onConfirmImport() {

        val uri = _state.value.pendingImportUri ?: return

        _state.value =
            _state.value.copy(
                showImportConfirm = false,
                isImporting = true
            )

        viewModelScope.launch {

            try {

                val resultado = importBackup(uri)

                _state.value =
                    _state.value.copy(
                        isImporting = false,
                        pendingImportUri = null,
                        importResult = resultado
                    )

            } catch (e: Exception) {

                _state.value =
                    _state.value.copy(
                        isImporting = false,
                        pendingImportUri = null,
                        importErrorRes = R.string.import_error
                    )

            }

        }

    }


    fun onImportResultShown() {

        _state.value =
            _state.value.copy(importResult = null)

    }


    fun onImportErrorShown() {

        _state.value =
            _state.value.copy(importErrorRes = null)

    }

}
