package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R

@Composable
fun PagamentoMesDialog(
    onMesAtual: () -> Unit,
    onMesPrevisto: () -> Unit,
    onCancel: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(stringResource(R.string.arecebimento_pergunta_mes_title))
        },
        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(stringResource(R.string.arecebimento_pergunta_mes_message))

                TextButton(
                    onClick = onMesAtual,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {

                    Text(stringResource(R.string.arecebimento_pergunta_mes_atual_option))

                }

                TextButton(
                    onClick = onMesPrevisto,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(stringResource(R.string.arecebimento_pergunta_mes_previsto_option))

                }

            }

        },
        confirmButton = {},
        dismissButton = {

            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }

        }
    )

}
