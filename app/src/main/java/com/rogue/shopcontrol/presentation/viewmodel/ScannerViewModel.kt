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

                    return@launch
                }


                val nfce =
                    parser.parse(html)

                savePurchase(
                    nfce
                )


                Log.d(
                    "NFC_E",
                    "Compra salva"
                )


            } catch (e: Exception) {

                Log.e(
                    "NFC_E",
                    "Erro ao processar NFC-e",
                    e
                )

            }

        }

    }


    fun resetScanner() {

        _state.value =
            ScannerState()

    }


    fun onError(
        message: String
    ) {

        _state.value =
            ScannerState(
                isScanning = false,
                error = message
            )

    }

}

