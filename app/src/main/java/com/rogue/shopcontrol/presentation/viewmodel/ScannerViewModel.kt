package com.rogue.shopcontrol.presentation.viewmodel

import android.util.Log
import com.rogue.shopcontrol.presentation.viewmodel.states.ScannerState

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rogue.shopcontrol.data.parser.NfceHtmlParser
import com.rogue.shopcontrol.data.remote.api.NfceApi
import com.rogue.shopcontrol.domain.usecase.SavePurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScannerViewModel(
    private val api: NfceApi,
    private val parser: NfceHtmlParser,
    private val savePurchase: SavePurchaseUseCase
) : ViewModel() {


    private val _state =
        MutableStateFlow(
            ScannerState()
        )

    val state =
        _state.asStateFlow()


    fun onQrCodeRead(url: String) {

        if (!_state.value.isScanning) {
            return
        }

        _state.value =
            _state.value.copy(
                isScanning = false
            )

        viewModelScope.launch {

            try {

                val html =
                    api.getNfceHtml(url)
                        .body()


                if (html == null) {

                    Log.e(
                        "NFC_E",
                        "HTML vazio"
                    )

                    _state.value = ScannerState(
                        isScanning = false,
                        error = "Não foi possível ler a nota fiscal"
                    )

                    return@launch
                }


                val nfce =
                    parser.parse(html)

                val compraId =
                    savePurchase(
                        nfce
                    )


                Log.d(
                    "NFC_E",
                    "Compra salva"
                )

                _state.value = ScannerState(
                    isScanning = false,
                    savedCompraId = compraId
                )


            } catch (e: Exception) {

                Log.e(
                    "NFC_E",
                    "Erro ao processar NFC-e",
                    e
                )

                _state.value = ScannerState(
                    isScanning = false,
                    error = "Erro ao processar a nota fiscal"
                )

            }

        }

    }


    fun resetScanner() {

        _state.value =
            ScannerState()

    }

}
