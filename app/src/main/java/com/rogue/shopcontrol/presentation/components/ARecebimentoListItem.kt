package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ARecebimentoComDados
import com.rogue.shopcontrol.presentation.viewmodel.states.isAtrasado
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun ARecebimentoListItem(
    item: ARecebimentoComDados,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val atrasado = item.isAtrasado()

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = if (atrasado) {
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        } else {
            CardDefaults.cardColors()
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = item.descricao,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${item.categoriaNome} • ${item.fonteNome}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = when {

                        item.pago ->
                            stringResource(
                                R.string.arecebimento_pago_em_label,
                                item.dataPagamento ?: item.dataPrevista
                            )

                        atrasado ->
                            stringResource(
                                R.string.arecebimento_atrasado_desde_label,
                                item.dataPrevista
                            )

                        else ->
                            stringResource(
                                R.string.arecebimento_data_prevista_label,
                                item.dataPrevista
                            )

                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (atrasado) FontWeight.Bold else FontWeight.Normal
                )

            }

            Text(
                text = formatCurrency(item.valor),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
