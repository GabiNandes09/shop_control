package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun PurchaseSummaryCard(
    compra: CompraCompleta,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = stringResource(R.string.purchase_summary_label),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            EstablishmentName(
                nome = compra.estabelecimento.nome,
                apelido = compra.estabelecimento.apelido,
                style = MaterialTheme.typography.titleLarge
            )

            if (compra.estabelecimento.cnpj.isNotBlank()) {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.cnpj_label, compra.estabelecimento.cnpj),
                    style = MaterialTheme.typography.bodySmall
                )

            }

            if (compra.estabelecimento.endereco.isNotBlank()) {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = compra.estabelecimento.endereco,
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = compra.compra.dataCompra ?: stringResource(R.string.data_not_informed),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.total_label, formatCurrency(compra.compra.valorTotal)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
