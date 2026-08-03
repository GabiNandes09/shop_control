package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun PurchaseListItem(
    compra: CompraCompleta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = compra.estabelecimento.nome,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = compra.compra.dataCompra ?: "Data não informada",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = formatCurrency(compra.compra.valorTotal),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
