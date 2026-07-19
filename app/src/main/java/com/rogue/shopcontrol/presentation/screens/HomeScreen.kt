package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun HomeScreen(
    onScannerClick: () -> Unit,
    onRecordsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),

        verticalArrangement = Arrangement.Center,

        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(
            onClick = onScannerClick
        ) {

            Text(
                text = "Scannear"
            )

        }


        Button(
            onClick = onRecordsClick
        ) {

            Text(
                text = "Registros"
            )

        }

    }

}