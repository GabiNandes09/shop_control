package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.data.local.entity.ProdutoCompraHistorico
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun ProductHistoryRow(
    historico: ProdutoCompraHistorico,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = historico.nomeEstabelecimento,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = historico.dataCompra ?: "Data não informada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Qtd: ${historico.quantidade} • Unit: ${formatCurrency(historico.valorUnitario)}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = formatCurrency(historico.valorTotal),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

            }

        }

    }

}
