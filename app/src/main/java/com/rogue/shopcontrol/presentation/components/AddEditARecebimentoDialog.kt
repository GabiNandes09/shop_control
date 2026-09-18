package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.foundation.clickable
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.FonteRendaEntity
import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.presentation.viewmodel.states.ARecebimentoListState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DATA_DISPLAY_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditARecebimentoDialog(
    state: ARecebimentoListState,
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
    onTipoChanged: (TipoARecebimento) -> Unit,
    onTotalParcelasChanged: (String) -> Unit,
    onShowDeleteConfirm: () -> Unit,
    onMarcarComoPagoClick: () -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(
                    if (state.isEdicao) R.string.edit_arecebimento_title else R.string.add_arecebimento_title
                )
            )
        },
        text = {

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {

                if (!state.isEdicao) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        listOf(TipoARecebimento.PONTUAL, TipoARecebimento.FIXA, TipoARecebimento.PARCELADA).forEach { tipo ->

                            FilterChip(
                                selected = state.tipo == tipo,
                                onClick = { onTipoChanged(tipo) },
                                label = {

                                    Text(
                                        stringResource(
                                            when (tipo) {
                                                TipoARecebimento.PONTUAL -> R.string.tipo_arecebimento_pontual
                                                TipoARecebimento.FIXA -> R.string.tipo_arecebimento_fixa
                                                TipoARecebimento.PARCELADA -> R.string.tipo_arecebimento_parcelada
                                            }
                                        )
                                    )

                                }
                            )

                        }

                    }

                    Spacer(Modifier.height(8.dp))

                }

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

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onShowDatePicker,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        stringResource(
                            R.string.arecebimento_data_prevista_label,
                            state.dataSelecionada.format(DATA_DISPLAY_FORMATTER)
                        )
                    )

                }

                if (!state.isEdicao && state.tipo == TipoARecebimento.PARCELADA) {

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.totalParcelasInput,
                        onValueChange = onTotalParcelasChanged,
                        label = {
                            Text(stringResource(R.string.compra_parcelada_total_label))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                }

                if (state.isEdicao && state.editingPago) {

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.arecebimento_pago_label),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }

                state.errorRes?.let { errorRes ->

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(errorRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )

                }

                if (state.isEdicao && !state.editingPago) {

                    Spacer(Modifier.height(8.dp))

                    TextButton(
                        onClick = onMarcarComoPagoClick
                    ) {

                        Text(stringResource(R.string.arecebimento_marcar_pago_button))

                    }

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
