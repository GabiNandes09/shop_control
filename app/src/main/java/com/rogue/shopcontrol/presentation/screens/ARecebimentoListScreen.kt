package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.TipoARecebimento
import com.rogue.shopcontrol.presentation.components.ARecebimentoListItem
import com.rogue.shopcontrol.presentation.components.AddEditARecebimentoDialog
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.PagamentoMesDialog
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.ARecebimentoListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ARecebimentoListScreen(
    highlightId: Long = 0L,
    onBackClick: () -> Unit,
    viewModel: ARecebimentoListViewModel = koinViewModel(
        parameters = { parametersOf(highlightId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = viewModel::onShowAddDialog
            ) {

                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_arecebimento_content_description)
                )

            }

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            ScreenHeader(
                title = stringResource(R.string.arecebimento_title),
                onBackClick = onBackClick
            )

            when {

                state.isLoading -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(stringResource(R.string.loading))

                    }

                }

                state.itens.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(stringResource(R.string.arecebimento_empty))

                    }

                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = state.itens,
                            key = { it.id }
                        ) { item ->

                            ARecebimentoListItem(
                                item = item,
                                onClick = {
                                    viewModel.onItemClick(item)
                                }
                            )

                        }

                    }

                }

            }

        }

    }

    if (state.showDialog) {

        AddEditARecebimentoDialog(
            state = state,
            onDescricaoChanged = viewModel::onDescricaoChanged,
            onValorChanged = viewModel::onValorChanged,
            onShowDatePicker = viewModel::onShowDatePicker,
            onDismissDatePicker = viewModel::onDismissDatePicker,
            onDataChanged = viewModel::onDataChanged,
            onShowCategoriaPicker = viewModel::onShowCategoriaPicker,
            onDismissCategoriaPicker = viewModel::onDismissCategoriaPicker,
            onCategoriaSelecionada = viewModel::onCategoriaSelecionada,
            onFonteInputChanged = viewModel::onFonteInputChanged,
            onFonteSugestaoSelecionada = viewModel::onFonteSugestaoSelecionada,
            onTipoChanged = viewModel::onTipoChanged,
            onTotalParcelasChanged = viewModel::onTotalParcelasChanged,
            onShowDeleteConfirm = viewModel::onShowDeleteConfirm,
            onMarcarComoPagoClick = viewModel::onMarcarComoPagoClick,
            onSave = viewModel::onSave,
            onDismiss = viewModel::onDismissDialog
        )

    }

    if (state.showDeleteConfirm) {

        ConfirmDialog(
            title = stringResource(R.string.delete_arecebimento_title),
            message = stringResource(
                when {
                    state.editingPago -> R.string.delete_arecebimento_pago_confirm_message
                    state.editingTipo != TipoARecebimento.PONTUAL -> R.string.delete_arecebimento_recorrente_confirm_message
                    else -> R.string.delete_arecebimento_confirm_message
                }
            ),
            confirmLabel = stringResource(R.string.delete),
            onConfirm = viewModel::onConfirmDelete,
            onDismiss = viewModel::onDismissDeleteConfirm
        )

    }

    if (state.showPagamentoMesDialog) {

        PagamentoMesDialog(
            onMesAtual = viewModel::onConfirmarMesAtual,
            onMesPrevisto = viewModel::onConfirmarMesPrevisto,
            onCancel = viewModel::onCancelarPagamentoMes
        )

    }

}
