package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.BarChart
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.model.ChartEntry

@Composable
fun AnalyticsScreen(
    title: String,
    entries: List<ChartEntry>,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = title,
            onBackClick = onBackClick
        )

        AnalyticsContent(
            entries = entries,
            isLoading = isLoading
        )

    }

}


@Composable
fun AnalyticsContent(
    entries: List<ChartEntry>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {

    when {

        isLoading -> {

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(stringResource(R.string.loading))

            }

        }

        entries.isEmpty() -> {

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(stringResource(R.string.analytics_empty))

            }

        }

        else -> {

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                analyticsChartItems(entries)

            }

        }

    }

}


fun LazyListScope.analyticsChartItems(
    entries: List<ChartEntry>
) {

    item {

        BarChart(
            entries = entries,
            modifier = Modifier.fillMaxWidth()
        )

    }

    item {

        HorizontalDivider()

    }

    items(entries) { entry ->

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = entry.label,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = entry.displayValue,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
