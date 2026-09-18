package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.AddEditRendaDialog
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.RendaListItem
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.RendaListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RendaListScreen(
    viewModel: RendaListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = viewModel::onShowAddDialog
            ) {

                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_renda_content_description)
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
            title = stringResource(R.string.renda_tab_label)
        )

        FilterToggleChip(
            expanded = filtersExpanded,
            onClick = { filtersExpanded = !filtersExpanded },
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

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

                        Text(stringResource(R.string.renda_empty))

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

                            RendaListItem(
                                item = item,
                                onClick = {
                                    viewModel.onItemClick(item)
                                }
                            )

                        }

                    }

                }

            }

            FilterOverlay(
                expanded = filtersExpanded,
                onDismiss = { filtersExpanded = false }
            ) {

                DateRangeSelector(
                    dateRange = state.dateRange,
                    onDateRangeChanged = viewModel::onDateRangeChanged
                )

            }

        }

    }

    }

    if (state.showDialog) {

        AddEditRendaDialog(
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
            onRecorrenteChanged = viewModel::onRecorrenteChanged,
            onShowDeleteConfirm = viewModel::onShowDeleteConfirm,
            onSave = viewModel::onSave,
            onDismiss = viewModel::onDismissDialog
        )

    }

    if (state.showDeleteConfirm) {

        ConfirmDialog(
            title = stringResource(R.string.delete_renda_title),
            message = stringResource(
                if (state.recorrente) {
                    R.string.delete_renda_recorrente_confirm_message
                } else {
                    R.string.delete_renda_confirm_message
                }
            ),
            confirmLabel = stringResource(R.string.delete),
            onConfirm = viewModel::onConfirmDelete,
            onDismiss = viewModel::onDismissDeleteConfirm
        )

    }

}
