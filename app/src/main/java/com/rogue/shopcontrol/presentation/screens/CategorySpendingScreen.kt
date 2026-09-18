package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.model.ChartEntry
import com.rogue.shopcontrol.presentation.viewmodel.CategorySpendingViewModel
import com.rogue.shopcontrol.presentation.viewmodel.states.CategorySpendingMode
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategorySpendingScreen(
    onBackClick: () -> Unit,
    viewModel: CategorySpendingViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    val semCategoriaLabel =
        stringResource(R.string.no_category)

    val entries =
        state.categorias.map { categoria ->

            ChartEntry(
                label = categoria.nome ?: semCategoriaLabel,
                value = categoria.valorTotalGasto,
                displayValue = formatCurrency(categoria.valorTotalGasto)
            )

        }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.category_spending_title),
            onBackClick = onBackClick
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(
                selected = state.modo == CategorySpendingMode.POR_COMPRA,
                onClick = { viewModel.onModoChanged(CategorySpendingMode.POR_COMPRA) },
                label = { Text(stringResource(R.string.category_spending_modo_por_compra)) }
            )

            FilterChip(
                selected = state.modo == CategorySpendingMode.POR_PRODUTO,
                onClick = { viewModel.onModoChanged(CategorySpendingMode.POR_PRODUTO) },
                label = { Text(stringResource(R.string.category_spending_modo_por_produto)) }
            )

        }

        FilterToggleChip(
            expanded = filtersExpanded,
            onClick = { filtersExpanded = !filtersExpanded },
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            AnalyticsContent(
                entries = entries,
                isLoading = state.isLoading
            )

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
