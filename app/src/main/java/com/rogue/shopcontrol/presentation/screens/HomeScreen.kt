package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.NegativeValueColor
import com.rogue.shopcontrol.presentation.components.PositiveValueColor
import com.rogue.shopcontrol.presentation.components.StatCard
import com.rogue.shopcontrol.presentation.viewmodel.HomeViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    onScannerClick: () -> Unit,
    onSaldoCardClick: () -> Unit,
    onManualEntryClick: () -> Unit,
    onAlertaClick: (Long) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var showFabMenu by remember {
        mutableStateOf(false)
    }

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {

            Box {

                FloatingActionButton(
                    onClick = { showFabMenu = true }
                ) {

                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = stringResource(R.string.scanner_content_description)
                    )

                }

                DropdownMenu(
                    expanded = showFabMenu,
                    onDismissRequest = { showFabMenu = false }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.scan_nfce_option))
                        },
                        onClick = {
                            showFabMenu = false
                            onScannerClick()
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.add_manual_option))
                        },
                        onClick = {
                            showFabMenu = false
                            onManualEntryClick()
                        }
                    )

                }

            }

        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                FilterToggleChip(
                    expanded = filtersExpanded,
                    onClick = { filtersExpanded = !filtersExpanded }
                )

                BadgedBox(
                    badge = {

                        if (state.alertasARecebimento.isNotEmpty()) {

                            Badge {
                                Text(state.alertasARecebimento.size.toString())
                            }

                        }

                    }
                ) {

                    IconButton(
                        onClick = viewModel::onShowAlertasDialog
                    ) {

                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = stringResource(R.string.arecebimento_alertas_content_description)
                        )

                    }

                }

            }

            StatCard(
                title = stringResource(R.string.balanco_saldo_title),
                value = formatCurrency(state.saldo),
                subtitle = stringResource(
                    R.string.balanco_saldo_composicao_label,
                    formatCurrency(state.valorRenda),
                    formatCurrency(state.valorGasto)
                ),
                valueColor = if (state.saldo >= 0) PositiveValueColor else NegativeValueColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onSaldoCardClick
            )

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

    if (state.showAlertasDialog) {

        AlertDialog(
            onDismissRequest = viewModel::onDismissAlertasDialog,
            title = {
                Text(stringResource(R.string.arecebimento_alertas_title))
            },
            text = {

                if (state.alertasARecebimento.isEmpty()) {

                    Text(stringResource(R.string.arecebimento_alertas_empty))

                } else {

                    Column {

                        state.alertasARecebimento.forEach { alerta ->

                            Text(
                                text = "${alerta.descricao} — ${formatCurrency(alerta.valor)}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onDismissAlertasDialog()
                                        onAlertaClick(alerta.id)
                                    }
                                    .padding(vertical = 12.dp)
                            )

                        }

                    }

                }

            },
            confirmButton = {

                TextButton(
                    onClick = viewModel::onDismissAlertasDialog
                ) {

                    Text(stringResource(R.string.ok))

                }

            }
        )

    }

}
