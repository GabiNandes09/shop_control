package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.RendaListItem
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.FonteDetailViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FonteDetailScreen(
    fonteId: Long,
    onBackClick: () -> Unit,
    viewModel: FonteDetailViewModel = koinViewModel(
        parameters = { parametersOf(fonteId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.fonte_detail_title),
            onBackClick = onBackClick
        )

        val fonte = state.fonte

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

            fonte == null -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(R.string.fonte_not_found))

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

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = fonte.nome,
                                        style = MaterialTheme.typography.titleLarge
                                    )

                                    Text(
                                        text = stringResource(
                                            R.string.total_label,
                                            formatCurrency(state.totalRecebido)
                                        ),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )

                                }

                            }

                        }

                        item {

                            FilterToggleChip(
                                expanded = filtersExpanded,
                                onClick = { filtersExpanded = !filtersExpanded }
                            )

                        }

                        if (state.lancamentos.isEmpty()) {

                            item {

                                Text(stringResource(R.string.fonte_lancamentos_empty))

                            }

                        } else {

                            items(
                                items = state.lancamentos,
                                key = { it.id }
                            ) { item ->

                                RendaListItem(
                                    item = item,
                                    onClick = {}
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

}
