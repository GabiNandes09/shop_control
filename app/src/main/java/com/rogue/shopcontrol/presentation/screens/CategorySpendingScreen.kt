package com.rogue.shopcontrol.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.model.ChartEntry
import com.rogue.shopcontrol.presentation.viewmodel.CategorySpendingViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategorySpendingScreen(
    onBackClick: () -> Unit,
    viewModel: CategorySpendingViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

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

    AnalyticsScreen(
        title = stringResource(R.string.category_spending_title),
        entries = entries,
        isLoading = state.isLoading,
        onBackClick = onBackClick
    )

}
