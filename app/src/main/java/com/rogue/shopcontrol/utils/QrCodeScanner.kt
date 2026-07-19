package com.rogue.shopcontrol.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

fun startScanner(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onQrCodeRead: (String) -> Unit
) {

    val cameraProviderFuture =
        ProcessCameraProvider.getInstance(context)


    cameraProviderFuture.addListener({

        val cameraProvider =
            cameraProviderFuture.get()


        val preview =
            Preview.Builder()
                .build()
                .also {

                    it.setSurfaceProvider(
                        previewView.surfaceProvider
                    )

                }


        val scanner =
            BarcodeScanning.getClient(
                BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE
                    )
                    .build()
            )


        val analyzer =
            ImageAnalysis.Builder()
                .setBackpressureStrategy(
                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                )
                .build()


        analyzer.setAnalyzer(
            ContextCompat.getMainExecutor(context)
        ) { imageProxy ->


            processImage(
                imageProxy,
                scanner,
                onQrCodeRead
            )


        }


        val cameraSelector =
            CameraSelector.DEFAULT_BACK_CAMERA


        try {


            cameraProvider.unbindAll()


            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                analyzer
            )


        } catch (e: Exception) {

            e.printStackTrace()

        }


    }, ContextCompat.getMainExecutor(context))

}

@OptIn(ExperimentalGetImage::class)
private fun processImage(
    imageProxy: ImageProxy,
    scanner: BarcodeScanner,
    onQrCodeRead: (String) -> Unit
) {
    var hasRead = false

    val mediaImage =
        imageProxy.image


    if (mediaImage != null) {


        val image =
            InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )


        scanner.process(image)
            .addOnSuccessListener { barcodes ->

                if (!hasRead) {

                    barcodes.firstOrNull()
                        ?.rawValue
                        ?.let {

                            hasRead = true
                            scanner.close()
                            onQrCodeRead(it)

                        }
                }

            }
            .addOnCompleteListener {

                imageProxy.close()

            }


    } else {

        imageProxy.close()

    }

}