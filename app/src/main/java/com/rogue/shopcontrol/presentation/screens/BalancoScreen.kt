package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.BalancoChart
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.BalancoViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import com.rogue.shopcontrol.utils.mesLabel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BalancoScreen(
    onBackClick: () -> Unit,
    viewModel: BalancoViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.balanco_title),
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

            state.entries.isEmpty() -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(R.string.analytics_empty))

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {

                        BalancoChart(
                            entries = state.entries,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                    item {
                        HorizontalDivider()
                    }

                    items(state.entries) { entry ->

                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                text = entry.mesLabel(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = stringResource(
                                        R.string.balanco_renda_label,
                                        formatCurrency(entry.renda)
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = stringResource(
                                        R.string.balanco_despesas_label,
                                        formatCurrency(entry.despesas)
                                    ),
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    text = stringResource(
                                        R.string.balanco_saldo_label,
                                        formatCurrency(entry.saldo)
                                    ),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )

                            }

                        }

                    }

                }

            }

        }

    }

}
