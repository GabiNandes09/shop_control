package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.EditApelidoDialog
import com.rogue.shopcontrol.presentation.components.EstablishmentSummaryCard
import com.rogue.shopcontrol.presentation.components.MonthSelector
import com.rogue.shopcontrol.presentation.components.PurchaseListItem
import com.rogue.shopcontrol.presentation.components.ScreenHeader
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.establishment_detail_title),
            onBackClick = onBackClick
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

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {

                        EstablishmentSummaryCard(
                            estabelecimento = estabelecimento,
                            totalGasto = state.totalGasto,
                            onEditClick = viewModel::onEditClick
                        )

                    }

                    if (state.chartEntries.isNotEmpty()) {

                        analyticsChartItems(state.chartEntries)

                    }

                    item {

                        MonthSelector(
                            selectedMonth = state.selectedMonth,
                            onPreviousMonth = viewModel::onPreviousMonth,
                            onNextMonth = viewModel::onNextMonth
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

            }

        }

    }

    if (state.showEditDialog) {

        EditApelidoDialog(
            value = state.editApelidoText,
            onValueChange = viewModel::onApelidoTextChanged,
            onSave = viewModel::onSaveApelido,
            onDismiss = viewModel::onEditDialogDismiss
        )

    }

}
