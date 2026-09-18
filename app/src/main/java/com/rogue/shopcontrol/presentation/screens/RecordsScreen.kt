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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.PurchaseListItem
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.RecordsViewModel
import com.rogue.shopcontrol.presentation.viewmodel.states.TipoFiltroCompra
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordsScreen(
    onPurchaseClick: (Long) -> Unit,
    onManualEntryClick: () -> Unit,
    viewModel: RecordsViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = onManualEntryClick
            ) {

                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_manual_option)
                )

            }

        }
    ) { innerPadding ->

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        ScreenHeader()

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

                state.compras.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(stringResource(R.string.records_empty))

                    }

                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        items(
                            items = state.compras,
                            key = { it.compra.id }
                        ) { compraCompleta ->

                            PurchaseListItem(
                                compra = compraCompleta,
                                onClick = {
                                    onPurchaseClick(compraCompleta.compra.id)
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

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = TipoFiltroCompra.entries
                    ) { tipo ->

                        FilterChip(
                            selected = state.tipoFiltro == tipo,
                            onClick = {
                                viewModel.onTipoFiltroChanged(tipo)
                            },
                            label = {

                                Text(
                                    stringResource(
                                        when (tipo) {
                                            TipoFiltroCompra.TODOS -> R.string.tipo_filtro_todos
                                            TipoFiltroCompra.VARIAVEIS -> R.string.tipo_filtro_variaveis
                                            TipoFiltroCompra.FIXAS -> R.string.tipo_filtro_fixas
                                            TipoFiltroCompra.PARCELADAS -> R.string.tipo_filtro_parceladas
                                            TipoFiltroCompra.RAPIDAS -> R.string.tipo_filtro_rapidas
                                        }
                                    )
                                )

                            }
                        )

                    }

                }

            }

        }

    }

    }

}
