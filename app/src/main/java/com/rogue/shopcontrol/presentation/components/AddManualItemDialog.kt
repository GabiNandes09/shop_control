package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity
import com.rogue.shopcontrol.presentation.viewmodel.states.ManualPurchaseEntryState

@Composable
fun AddManualItemDialog(
    state: ManualPurchaseEntryState,
    onNomeChanged: (String) -> Unit,
    onSugestaoSelecionada: (ProdutoEntity) -> Unit,
    onQuantidadeChanged: (String) -> Unit,
    onValorUnitarioChanged: (String) -> Unit,
    onValorTotalChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.add_item_content_description))
        },
        text = {

            Column {

                OutlinedTextField(
                    value = state.itemNomeInput,
                    onValueChange = onNomeChanged,
                    label = {
                        Text(stringResource(R.string.product_name_label))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                state.produtosSugeridos.forEach { sugestao ->

                    Text(
                        text = sugestao.nome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSugestaoSelecionada(sugestao)
                            }
                            .padding(vertical = 8.dp)
                    )

                }

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.itemQuantidade,
                    onValueChange = onQuantidadeChanged,
                    label = {
                        Text(stringResource(R.string.item_quantity_label))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.itemValorUnitario,
                    onValueChange = onValorUnitarioChanged,
                    label = {
                        Text(stringResource(R.string.item_unit_price_label))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.itemValorTotal,
                    onValueChange = onValorTotalChanged,
                    label = {
                        Text(stringResource(R.string.item_total_price_label))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                state.itemErrorRes?.let { errorRes ->

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(errorRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )

                }

            }

        },
        confirmButton = {

            TextButton(
                onClick = onConfirm
            ) {

                Text(stringResource(R.string.add))

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

}
