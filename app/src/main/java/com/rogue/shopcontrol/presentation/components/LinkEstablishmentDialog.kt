package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
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
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity

@Composable
fun LinkEstablishmentDialog(
    estabelecimentos: List<EstabelecimentoEntity>,
    onEstablishmentSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.link_establishment_picker_title))
        },
        text = {

            if (estabelecimentos.isEmpty()) {

                Text(stringResource(R.string.link_establishment_none_available))

            } else {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    estabelecimentos.forEach { estabelecimento ->

                        Text(
                            text = estabelecimento.apelido ?: estabelecimento.nome,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onEstablishmentSelected(estabelecimento.id)
                                }
                                .padding(vertical = 12.dp)
                        )

                    }

                }

            }

        },
        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(stringResource(R.string.cancel))

            }

        }
    )

}
