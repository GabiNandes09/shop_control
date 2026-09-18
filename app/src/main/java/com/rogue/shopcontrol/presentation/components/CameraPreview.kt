package com.rogue.shopcontrol.presentation.components

import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.common.Barcode
import com.rogue.shopcontrol.utils.startScanner

@Composable
fun CameraPreview(
    lifecycleOwner: LifecycleOwner,
    onQrCodeRead: (String) -> Unit,
    formats: List<Int> = listOf(Barcode.FORMAT_QR_CODE)
) {

    val context = LocalContext.current
    var previewView by remember {
        mutableStateOf<PreviewView?>(null)
    }


    AndroidView(
        factory = { ctx ->

            PreviewView(ctx).also {

                previewView = it

            }

        }
    )

    LaunchedEffect(previewView) {

        previewView?.let {

            startScanner(
                context = context,
                lifecycleOwner = lifecycleOwner,
                previewView = it,
                onQrCodeRead = onQrCodeRead,
                formats = formats
            )

        }

    }
}
