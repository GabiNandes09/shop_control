package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.presentation.viewmodel.states.RendaListState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DATA_DISPLAY_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRendaDialog(
    state: RendaListState,
    onDescricaoChanged: (String) -> Unit,
    onValorChanged: (String) -> Unit,
    onShowDatePicker: () -> Unit,
    onDismissDatePicker: () -> Unit,
    onDataChanged: (LocalDate) -> Unit,
    onShowCategoriaPicker: () -> Unit,
    onDismissCategoriaPicker: () -> Unit,
    onCategoriaSelecionada: (Long) -> Unit,
    onFonteInputChanged: (String) -> Unit,
    onFonteSugestaoSelecionada: (FonteRendaEntity) -> Unit,
    onRecorrenteChanged: (Boolean) -> Unit,
    onShowDeleteConfirm: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(
                    if (state.isEdicao) R.string.edit_renda_title else R.string.add_renda_title
                )
            )
        },
        text = {

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {

                OutlinedTextField(
                    value = state.descricao,
                    onValueChange = onDescricaoChanged,
                    label = {
                        Text(stringResource(R.string.renda_descricao_label))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.valorInput,
                    onValueChange = onValorChanged,
                    label = {
                        Text(stringResource(R.string.renda_valor_label))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                val categoriaSelecionadaNome =
                    state.categorias
                        .firstOrNull { it.id == state.selectedCategoriaId }
                        ?.nome
                        ?: stringResource(R.string.select_category_title)

                OutlinedButton(
                    onClick = onShowCategoriaPicker,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(categoriaSelecionadaNome)

                }

                Spacer(Modifier.height(8.dp))

                Column {

                    OutlinedTextField(
                        value = state.fonteInput,
                        onValueChange = onFonteInputChanged,
                        label = {
                            Text(stringResource(R.string.renda_fonte_label))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    state.fontesSugeridas.forEach { sugestao ->

                        Text(
                            text = sugestao.nome,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onFonteSugestaoSelecionada(sugestao)
                                }
                                .padding(vertical = 8.dp)
                        )

                    }

                }

                if (!state.isEdicao) {

                    Spacer(Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onShowDatePicker,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            stringResource(
                                R.string.purchase_date_label,
                                state.dataSelecionada.format(DATA_DISPLAY_FORMATTER)
                            )
                        )

                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(R.string.renda_recorrente_label),
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = state.recorrente,
                            onCheckedChange = onRecorrenteChanged
                        )

                    }

                }

                state.errorRes?.let { errorRes ->

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(errorRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )

                }

                if (state.isEdicao) {

                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = onShowDeleteConfirm
                    ) {

                        Text(
                            text = stringResource(R.string.delete),
                            color = MaterialTheme.colorScheme.error
                        )

                    }

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

    if (state.showDatePicker) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    state.dataSelecionada.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )

        DatePickerDialog(
            onDismissRequest = onDismissDatePicker,
            confirmButton = {

                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

                            val data =
                                Instant.ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()

                            onDataChanged(data)

                        }

                    }
                ) {

                    Text(stringResource(R.string.ok))

                }

            },
            dismissButton = {

                TextButton(
                    onClick = onDismissDatePicker
                ) {

                    Text(stringResource(R.string.cancel))

                }

            }
        ) {

            DatePicker(state = datePickerState)

        }

    }

    if (state.showCategoriaPicker) {

        RendaCategoriaPickerDialog(
            categorias = state.categorias,
            onCategoriaSelected = onCategoriaSelecionada,
            onDismiss = onDismissCategoriaPicker
        )

    }

}
