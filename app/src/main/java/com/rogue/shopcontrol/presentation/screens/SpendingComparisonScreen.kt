package com.rogue.shopcontrol.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.viewmodel.SpendingComparisonViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpendingComparisonScreen(
    onBackClick: () -> Unit,
    viewModel: SpendingComparisonViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AnalyticsScreen(
        title = stringResource(R.string.spending_comparison_title),
        entries = state.entries,
        isLoading = state.isLoading,
        onBackClick = onBackClick
    )

}
