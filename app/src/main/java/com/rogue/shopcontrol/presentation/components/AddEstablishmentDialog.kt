package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity

@Composable
fun AddEstablishmentDialog(
    nome: String,
    onNomeChanged: (String) -> Unit,
    cnpj: String,
    onCnpjChanged: (String) -> Unit,
    endereco: String,
    onEnderecoChanged: (String) -> Unit,
    apelido: String,
    onApelidoChanged: (String) -> Unit,
    categorias: List<CategoriaEntity>,
    categoriaSelecionadaId: Long?,
    onShowCategoriaPicker: () -> Unit,
    errorRes: Int?,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.add_establishment_dialog_title))
        },
        text = {

            Column {

                OutlinedTextField(
                    value = nome,
                    onValueChange = onNomeChanged,
                    label = {
                        Text(stringResource(R.string.establishment_name_label))
                    },
                    singleLine = true,
                    isError = errorRes != null,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = cnpj,
                    onValueChange = onCnpjChanged,
                    label = {
                        Text(stringResource(R.string.cnpj_optional_label))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = endereco,
                    onValueChange = onEnderecoChanged,
                    label = {
                        Text(stringResource(R.string.establishment_address_label))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = apelido,
                    onValueChange = onApelidoChanged,
                    label = {
                        Text(stringResource(R.string.apelido_label))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                val categoriaSelecionadaNome =
                    categorias
                        .firstOrNull { it.id == categoriaSelecionadaId }
                        ?.nome
                        ?: stringResource(R.string.select_category_title)

                OutlinedButton(
                    onClick = onShowCategoriaPicker,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(categoriaSelecionadaNome)

                }

                errorRes?.let {

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )

                }

            }

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

}
