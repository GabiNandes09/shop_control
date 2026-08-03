package com.rogue.shopcontrol.presentation.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.components.SuccessDialog
import com.rogue.shopcontrol.presentation.viewmodel.ScannerViewModel
import com.rogue.shopcontrol.utils.startScanner
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = koinViewModel(),
    onPurchaseSaved: (Long) -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var hasPermission by remember {
        mutableStateOf(false)
    }


    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasPermission = granted

            if (!granted) {
                Toast.makeText(
                    context,
                    "Permissão da câmera necessária",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    LaunchedEffect(Unit) {

        permissionLauncher.launch(
            Manifest.permission.CAMERA
        )

    }


    if (hasPermission) {

        CameraPreview(
            lifecycleOwner = lifecycleOwner,
            onQrCodeRead = { viewModel.onQrCodeRead(it) }
        )

    }


    state.savedCompraId?.let { compraId ->

        SuccessDialog(
            title = "Compra salva",
            message = "A nota fiscal foi lida e salva com sucesso.",
            confirmLabel = "Ver detalhes",
            onConfirm = {
                viewModel.resetScanner()
                onPurchaseSaved(compraId)
            }
        )

    }

}


@Composable
fun CameraPreview(
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onQrCodeRead: (String) -> Unit
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
                context,
                lifecycleOwner,
                it,
                onQrCodeRead
            )

        }

    }
}
