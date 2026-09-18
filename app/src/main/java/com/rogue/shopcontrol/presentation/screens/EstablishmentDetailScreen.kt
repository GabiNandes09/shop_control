package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.rogue.shopcontrol.presentation.components.CategoryPickerDialog
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.EstablishmentSummaryCard
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.LinkEstablishmentDialog
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.PurchaseListItem
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.components.TextInputDialog
import com.rogue.shopcontrol.presentation.viewmodel.EstablishmentDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EstablishmentDetailScreen(
    estabelecimentoId: Long,
    onBackClick: () -> Unit,
    onPurchaseClick: (Long) -> Unit,
    viewModel: EstablishmentDetailViewModel = koinViewModel(
        parameters = { parametersOf(estabelecimentoId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.isLinked) {

        if (state.isLinked) {
            onBackClick()
        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.establishment_detail_title),
            onBackClick = onBackClick,
            actions = {

                if (state.estabelecimento?.cnpj?.isBlank() == true) {

                    var showMenu by remember {
                        mutableStateOf(false)
                    }

                    IconButton(
                        onClick = { showMenu = true }
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.establishment_options_content_description)
                        )

                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text(stringResource(R.string.link_establishment_button))
                            },
                            onClick = {
                                showMenu = false
                                viewModel.onShowLinkPicker()
                            }
                        )

                    }

                }

            }
        )

        val estabelecimento = state.estabelecimento

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

            estabelecimento == null -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(R.string.establishment_not_found))

                }

            }

            else -> {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        item {

                            EstablishmentSummaryCard(
                                estabelecimento = estabelecimento,
                                totalGasto = state.totalGasto,
                                categoriaNome = state.categoriaNome,
                                onEditClick = viewModel::onEditClick,
                                onEditCategoriaClick = viewModel::onShowCategoryPicker
                            )

                        }

                        if (state.chartEntries.isNotEmpty()) {

                            analyticsChartItems(state.chartEntries)

                        }

                        item {

                            FilterToggleChip(
                                expanded = filtersExpanded,
                                onClick = { filtersExpanded = !filtersExpanded }
                            )

                        }

                        if (state.compras.isEmpty()) {

                            item {

                                Text(stringResource(R.string.establishment_purchases_empty))

                            }

                        } else {

                            items(
                                items = state.compras,
                                key = { it.compra.id }
                            ) { compra ->

                                PurchaseListItem(
                                    compra = compra,
                                    onClick = {
                                        onPurchaseClick(compra.compra.id)
                                    }
                                )

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

    }

    if (state.showEditDialog) {

        TextInputDialog(
            title = stringResource(R.string.edit_apelido_title),
            label = stringResource(R.string.apelido_label),
            value = state.editApelidoText,
            onValueChange = viewModel::onApelidoTextChanged,
            onSave = viewModel::onSaveApelido,
            onDismiss = viewModel::onEditDialogDismiss
        )

    }

    if (state.showLinkPicker) {

        LinkEstablishmentDialog(
            estabelecimentos = state.estabelecimentosComCnpj,
            onEstablishmentSelected = viewModel::onLinkTargetSelected,
            onDismiss = viewModel::onDismissLinkPicker
        )

    }

    if (state.showLinkConfirm) {

        ConfirmDialog(
            title = stringResource(R.string.link_establishment_confirm_title),
            message = stringResource(R.string.link_establishment_confirm_message),
            onConfirm = viewModel::onConfirmLink,
            onDismiss = viewModel::onDismissLinkConfirm
        )

    }

    if (state.showCategoryPicker) {

        CategoryPickerDialog(
            categorias = state.categorias,
            onCategorySelected = viewModel::onCategoriaSelecionada,
            onDismiss = viewModel::onDismissCategoryPicker,
            onCreateCategory = viewModel::onCreateCategoria
        )

    }

    if (state.showCategoriaConflictDialog) {

        val nomeOrigem =
            state.categorias.firstOrNull { it.id == state.categoriaConflictOrigemId }?.nome ?: ""

        val nomeDestino =
            state.categorias.firstOrNull { it.id == state.categoriaConflictDestinoId }?.nome ?: ""

        AlertDialog(
            onDismissRequest = viewModel::onDismissCategoriaConflictDialog,
            title = {
                Text(stringResource(R.string.categoria_conflict_title))
            },
            text = {
                Text(stringResource(R.string.categoria_conflict_message, nomeOrigem, nomeDestino))
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        state.categoriaConflictDestinoId?.let(viewModel::onCategoriaConflictResolved)
                    }
                ) {
                    Text(nomeDestino)
                }

            },
            dismissButton = {

                TextButton(
                    onClick = {
                        state.categoriaConflictOrigemId?.let(viewModel::onCategoriaConflictResolved)
                    }
                ) {
                    Text(nomeOrigem)
                }

            }
        )

    }

}
