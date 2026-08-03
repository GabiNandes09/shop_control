package com.rogue.shopcontrol.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.viewmodel.SpendingComparisonViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpendingComparisonScreen(
    viewModel: SpendingComparisonViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AnalyticsScreen(
        title = "Comparação de Gastos",
        entries = state.entries,
        isLoading = state.isLoading
    )

}
