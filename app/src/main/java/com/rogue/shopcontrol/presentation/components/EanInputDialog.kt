package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rogue.shopcontrol.R

@Composable
fun EanInputDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {

    var showScanner by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.ean_dialog_title))
        },
        text = {

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(stringResource(R.string.ean_label))
                },
                singleLine = true,
                trailingIcon = {

                    IconButton(
                        onClick = { showScanner = true }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = stringResource(R.string.scan_barcode_content_description)
                        )

                    }

                },
                modifier = Modifier.fillMaxWidth()
            )

        },
        confirmButton = {

            TextButton(
                onClick = onSave
            ) {

                Text(stringResource(R.string.save))

            }

        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(stringResource(R.string.cancel))

            }

        }
    )

    if (showScanner) {

        BarcodeScannerDialog(
            onDismiss = { showScanner = false },
            onBarcodeScanned = { codigo ->
                onValueChange(codigo)
                showScanner = false
            }
        )

    }

}
