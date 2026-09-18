package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CompraEntity
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun ParcelaRow(
    parcela: CompraEntity,
    numero: Int,
    isAtual: Boolean,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = if (isAtual) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = stringResource(
                    if (isAtual) {
                        R.string.parcela_numero_atual_label
                    } else {
                        R.string.parcela_numero_label
                    },
                    numero,
                    parcela.totalParcelas ?: numero
                ),
                fontWeight = if (isAtual) FontWeight.Bold else FontWeight.Normal
            )

            Text(
                text = formatCurrency(parcela.valorTotal),
                fontWeight = FontWeight.Bold
            )

        }

    }

}
