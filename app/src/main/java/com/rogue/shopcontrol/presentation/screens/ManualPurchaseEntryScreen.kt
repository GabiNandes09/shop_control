package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.presentation.components.AddManualItemDialog
import com.rogue.shopcontrol.presentation.components.CategoryPickerDialog
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.ManualPurchaseItemRow
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.ManualPurchaseEntryViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DATA_DISPLAY_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualPurchaseEntryScreen(
    compraId: Long,
    onBackClick: () -> Unit,
    onSaved: (Long) -> Unit,
    viewModel: ManualPurchaseEntryViewModel = koinViewModel(
        parameters = { parametersOf(compraId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.savedCompraId) {

        state.savedCompraId?.let { idSalvo ->
            onSaved(idSalvo)
        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(
                if (state.isEdicao) R.string.edit_purchase else R.string.manual_purchase_title
            ),
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            if (!state.isEdicao) {

                item {

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = listOf(TipoCompra.VARIAVEL, TipoCompra.FIXA, TipoCompra.PARCELADA, TipoCompra.RAPIDA)
                        ) { tipo ->

                            FilterChip(
                                selected = state.tipoCompra == tipo,
                                onClick = {
                                    viewModel.onTipoChanged(tipo)
                                },
                                label = {

                                    Text(
                                        stringResource(
                                            when (tipo) {
                                                TipoCompra.VARIAVEL -> R.string.tipo_compra_variavel
                                                TipoCompra.FIXA -> R.string.tipo_compra_fixa
                                                TipoCompra.PARCELADA -> R.string.tipo_compra_parcelada
                                                TipoCompra.RAPIDA -> R.string.tipo_compra_rapida
                                            }
                                        )
                                    )

                                }
                            )

                        }

                    }

                }

            }

            item {

                Column {

                    OutlinedTextField(
                        value = state.estabelecimentoNome,
                        onValueChange = viewModel::onEstabelecimentoNomeChanged,
                        label = {
                            Text(stringResource(R.string.establishment_name_label))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    state.estabelecimentosSugeridos.forEach { sugestao ->

                        Text(
                            text = sugestao.apelido ?: sugestao.nome,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onEstabelecimentoSugestaoSelecionada(sugestao)
                                }
                                .padding(vertical = 8.dp)
                        )

                    }

                }

            }

            if (!state.estabelecimentoExistente) {

                item {

                    OutlinedTextField(
                        value = state.estabelecimentoApelido,
                        onValueChange = viewModel::onEstabelecimentoApelidoChanged,
                        label = {
                            Text(stringResource(R.string.apelido_label))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                }

            }

            item {

                OutlinedButton(
                    onClick = viewModel::onShowDatePicker,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        stringResource(
                            R.string.purchase_date_label,
                            state.dataCompra.format(DATA_DISPLAY_FORMATTER)
                        )
                    )

                }

            }

            if (state.tipoCompra == TipoCompra.VARIAVEL) {

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(R.string.items_section_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = viewModel::onShowAddItemDialog
                        ) {

                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add_item_content_description)
                            )

                        }

                    }

                }

                items(
                    items = state.itens,
                    key = { it.produtoId }
                ) { item ->

                    ManualPurchaseItemRow(
                        item = item,
                        onRemove = {
                            viewModel.onRemoveItem(item.produtoId)
                        }
                    )

                }

                item {

                    Text(
                        text = stringResource(R.string.total_label, formatCurrency(state.valorTotalCompra)),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                }

                item {

                    val categoriaSelecionadaNome =
                        state.categorias
                            .firstOrNull { it.id == state.selectedCategoriaId }
                            ?.nome
                            ?: stringResource(R.string.no_category)

                    OutlinedButton(
                        onClick = viewModel::onShowCategoriaPicker,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(categoriaSelecionadaNome)

                    }

                }

            } else {

                item {

                    OutlinedTextField(
                        value = state.nomeCompra,
                        onValueChange = viewModel::onNomeCompraChanged,
                        label = {
                            Text(stringResource(R.string.compra_fixa_nome_label))
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                }

                item {

                    OutlinedTextField(
                        value = state.valorCompraInput,
                        onValueChange = viewModel::onValorCompraChanged,
                        label = {
                            Text(stringResource(R.string.compra_fixa_valor_label))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                }

                item {

                    val categoriaSelecionadaNome =
                        state.categorias
                            .firstOrNull { it.id == state.selectedCategoriaId }
                            ?.nome
                            ?: stringResource(R.string.select_category_title)

                    OutlinedButton(
                        onClick = viewModel::onShowCategoriaPicker,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(categoriaSelecionadaNome)

                    }

                }

                if (state.tipoCompra == TipoCompra.PARCELADA) {

                    item {

                        OutlinedTextField(
                            value = state.totalParcelasInput,
                            onValueChange = viewModel::onTotalParcelasChanged,
                            label = {
                                Text(stringResource(R.string.compra_parcelada_total_label))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            enabled = !state.isEdicao,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                    if (!state.isEdicao) {

                        item {

                            OutlinedTextField(
                                value = state.parcelasJaPagasInput,
                                onValueChange = viewModel::onParcelasJaPagasChanged,
                                label = {
                                    Text(stringResource(R.string.compra_parcelada_ja_pagas_label))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                        }

                    }

                }

            }

            if (!state.isEdicao) {

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(R.string.dividir_conta_label),
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = state.dividirConta,
                            onCheckedChange = viewModel::onDividirContaChanged
                        )

                    }

                }

                if (state.dividirConta) {

                    item {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(R.string.dividir_igualmente_label),
                                modifier = Modifier.weight(1f)
                            )

                            Switch(
                                checked = state.dividirIgualmente,
                                onCheckedChange = viewModel::onDividirIgualmenteChanged
                            )

                        }

                    }

                    item {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                OutlinedTextField(
                                    value = state.participanteInput,
                                    onValueChange = viewModel::onParticipanteInputChanged,
                                    label = {
                                        Text(stringResource(R.string.divisao_participante_label))
                                    },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )

                                IconButton(
                                    onClick = viewModel::onConfirmAddParticipante
                                ) {

                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = stringResource(R.string.divisao_add_participante_content_description)
                                    )

                                }

                            }

                            state.participantesSugeridos.forEach { sugestao ->

                                Text(
                                    text = sugestao.nome,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.onParticipanteSugestaoSelecionada(sugestao)
                                        }
                                        .padding(vertical = 8.dp)
                                )

                            }

                            state.divisaoErrorRes?.let { errorRes ->

                                Text(
                                    text = stringResource(errorRes),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )

                            }

                        }

                    }

                    items(
                        items = state.participantes,
                        key = { it.fonteId }
                    ) { participante ->

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = participante.nome,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = participante.valorInput,
                                onValueChange = { texto ->
                                    viewModel.onParticipanteValorChanged(participante.fonteId, texto)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    viewModel.onRemoveParticipante(participante.fonteId)
                                }
                            ) {

                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(R.string.delete)
                                )

                            }

                        }

                    }

                    item {

                        Text(
                            text = stringResource(R.string.divisao_sua_parte_label, formatCurrency(state.suaParte)),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                    }

                }

            }

        }

        Button(
            onClick = viewModel::onSave,
            enabled = state.podeSalvar && !state.isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(stringResource(R.string.save))

        }

    }

    if (state.showDatePicker) {

        val datePickerState =
            rememberDatePickerState(
                initialSelectedDateMillis =
                    state.dataCompra.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )

        DatePickerDialog(
            onDismissRequest = viewModel::onDismissDatePicker,
            confirmButton = {

                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let { millis ->

                            val data =
                                Instant.ofEpochMilli(millis)
                                    .atZone(ZoneOffset.UTC)
                                    .toLocalDate()

                            viewModel.onDataChanged(data)

                        }

                    }
                ) {

                    Text(stringResource(R.string.ok))

                }

            },
            dismissButton = {

                TextButton(
                    onClick = viewModel::onDismissDatePicker
                ) {

                    Text(stringResource(R.string.cancel))

                }

            }
        ) {

            DatePicker(state = datePickerState)

        }

    }

    if (state.showAddItemDialog) {

        AddManualItemDialog(
            state = state,
            onNomeChanged = viewModel::onItemNomeChanged,
            onSugestaoSelecionada = viewModel::onItemSugestaoSelecionada,
            onQuantidadeChanged = viewModel::onItemQuantidadeChanged,
            onValorUnitarioChanged = viewModel::onItemValorUnitarioChanged,
            onValorTotalChanged = viewModel::onItemValorTotalChanged,
            onConfirm = viewModel::onConfirmAddItem,
            onDismiss = viewModel::onDismissAddItemDialog
        )

    }

    if (state.showConfirmNewProduct) {

        ConfirmDialog(
            title = stringResource(R.string.confirm_new_product_title),
            message = stringResource(R.string.confirm_new_product_message, state.pendingNovoProdutoNome),
            onConfirm = viewModel::onConfirmNewProduct,
            onDismiss = viewModel::onDismissConfirmNewProduct
        )

    }

    if (state.showCategoriaPicker) {

        CategoryPickerDialog(
            categorias = state.categorias,
            onCategorySelected = viewModel::onCategoriaSelecionada,
            onDismiss = viewModel::onDismissCategoriaPicker,
            onCreateCategory = viewModel::onCreateCategoria
        )

    }

}
