package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.presentation.model.ChartEntry

@Composable
fun BarChart(
    entries: List<ChartEntry>,
    modifier: Modifier = Modifier
) {

    val barColor = MaterialTheme.colorScheme.primary
    val maxValue = entries.maxOfOrNull { it.value } ?: 0.0

    Column(
        modifier = modifier
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {

            if (entries.isEmpty() || maxValue <= 0.0) {
                return@Canvas
            }

            val slot = size.width / entries.size
            val barWidth = slot * 0.5f

            entries.forEachIndexed { index, entry ->

                val barHeight =
                    size.height * (entry.value / maxValue).toFloat()

                drawRect(
                    color = barColor,
                    topLeft = Offset(
                        x = slot * index + (slot - barWidth) / 2f,
                        y = size.height - barHeight
                    ),
                    size = Size(
                        width = barWidth,
                        height = barHeight
                    )
                )

            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {

            entries.forEach { entry ->

                Text(
                    text = entry.label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )

            }

        }

    }

}
