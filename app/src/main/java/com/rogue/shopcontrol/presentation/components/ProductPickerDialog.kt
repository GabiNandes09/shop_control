package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity

@Composable
fun ProductPickerDialog(
    produtos: List<ProdutoEntity>,
    onProductSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.merge_product_picker_title))
        },
        text = {

            if (produtos.isEmpty()) {

                Text(stringResource(R.string.merge_product_none_available))

            } else {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    produtos.forEach { produto ->

                        Text(
                            text = produto.apelido ?: produto.nome,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onProductSelected(produto.id)
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
