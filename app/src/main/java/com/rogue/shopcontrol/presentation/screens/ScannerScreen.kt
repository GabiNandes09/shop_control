package com.rogue.shopcontrol.presentation.screens

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.CameraPreview
import com.rogue.shopcontrol.presentation.components.SuccessDialog
import com.rogue.shopcontrol.presentation.viewmodel.ScannerViewModel
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

    val cameraPermissionRequiredMessage =
        stringResource(R.string.camera_permission_required)


    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasPermission = granted

            if (!granted) {
                Toast.makeText(
                    context,
                    cameraPermissionRequiredMessage,
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


    if (state.isLoading) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Text(
                    text = stringResource(R.string.scanner_loading),
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )

            }

        }

    }



    state.savedCompraId?.let { compraId ->

        SuccessDialog(
            title = stringResource(R.string.purchase_saved_title),
            message = stringResource(R.string.purchase_saved_message),
            confirmLabel = stringResource(R.string.view_details),
            onConfirm = {
                viewModel.resetScanner()
                onPurchaseSaved(compraId)
            }
        )

    }


    LaunchedEffect(state.errorRes) {

        state.errorRes?.let { errorRes ->

            Toast.makeText(
                context,
                context.getString(errorRes),
                Toast.LENGTH_SHORT
            ).show()

            viewModel.resetScanner()

        }

    }

}
