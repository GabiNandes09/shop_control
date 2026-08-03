package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(
    onScannerClick: () -> Unit,
    onRecordsClick: () -> Unit
) {

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = onScannerClick
            ) {

                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Scanner"
                )

            }

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Button(
                onClick = onRecordsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text("Registros")

            }

        }

    }

}
