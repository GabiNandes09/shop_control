package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.domain.model.BalancoMensal
import com.rogue.shopcontrol.utils.mesLabel
import kotlin.math.max

@Composable
fun BalancoChart(
    entries: List<BalancoMensal>,
    modifier: Modifier = Modifier
) {

    val rendaColor = MaterialTheme.colorScheme.primary
    val despesaColor = MaterialTheme.colorScheme.error
    val saldoColor = MaterialTheme.colorScheme.onSurface
    val baselineColor = MaterialTheme.colorScheme.outline

    val maxPositivo =
        max(
            entries.maxOfOrNull { maxOf(it.renda, it.despesas, it.saldo) } ?: 0.0,
            0.0
        )

    val maxNegativo =
        max(
            entries.maxOfOrNull { -it.saldo } ?: 0.0,
            0.0
        )

    val totalRange = maxPositivo + maxNegativo

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.padding(bottom = 8.dp)
        ) {

            LegendaItem(cor = rendaColor, texto = stringResource(R.string.balanco_legenda_renda))

            Row(modifier = Modifier.width(16.dp)) {}

            LegendaItem(cor = despesaColor, texto = stringResource(R.string.balanco_legenda_despesas))

            Row(modifier = Modifier.width(16.dp)) {}

            LegendaItem(cor = saldoColor, texto = stringResource(R.string.balanco_legenda_saldo))

        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            if (entries.isEmpty() || totalRange <= 0.0) {
                return@Canvas
            }

            fun yDe(valor: Double): Float =
                (size.height * (maxPositivo - valor) / totalRange).toFloat()

            val baselineY = yDe(0.0)

            drawLine(
                color = baselineColor,
                start = Offset(0f, baselineY),
                end = Offset(size.width, baselineY),
                strokeWidth = 2f
            )

            val slot = size.width / entries.size
            val barWidth = slot * 0.3f

            val pontosSaldo = mutableListOf<Offset>()

            entries.forEachIndexed { index, entry ->

                val baseX = slot * index

                val rendaY = yDe(entry.renda)
                drawRect(
                    color = rendaColor,
                    topLeft = Offset(baseX + slot * 0.15f, rendaY),
                    size = Size(barWidth, baselineY - rendaY)
                )

                val despesaY = yDe(entry.despesas)
                drawRect(
                    color = despesaColor,
                    topLeft = Offset(baseX + slot * 0.55f, despesaY),
                    size = Size(barWidth, baselineY - despesaY)
                )

                pontosSaldo.add(
                    Offset(baseX + slot / 2f, yDe(entry.saldo))
                )

            }

            for (i in 0 until pontosSaldo.size - 1) {

                drawLine(
                    color = saldoColor,
                    start = pontosSaldo[i],
                    end = pontosSaldo[i + 1],
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )

            }

            pontosSaldo.forEach { ponto ->

                drawCircle(
                    color = saldoColor,
                    radius = 6f,
                    center = ponto,
                    style = Stroke(width = 3f)
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
                    text = entry.mesLabel(),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )

            }

        }

    }

}


@Composable
private fun LegendaItem(
    cor: Color,
    texto: String
) {

    Row {

        Box(
            modifier = Modifier
                .size(10.dp)
                .padding(top = 4.dp)
        ) {

            Canvas(modifier = Modifier.size(10.dp)) {
                drawCircle(color = cor)
            }

        }

        Row(modifier = Modifier.width(4.dp)) {}

        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall
        )

    }

}
