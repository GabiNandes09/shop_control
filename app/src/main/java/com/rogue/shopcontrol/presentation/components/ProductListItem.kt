package com.rogue.shopcontrol.presentation.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ItemCompraCompleto
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun ProductListItem(
    itemCompleto: ItemCompraCompleto,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = itemCompleto.produto.nome,
                style = MaterialTheme.typography.titleMedium
            )

            itemCompleto.produto.codigo?.let { codigo ->

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.product_code_label, codigo),
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.quantity_label, itemCompleto.item.quantidade),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.unit_price_label,
                    formatCurrency(itemCompleto.item.valorUnitario)
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.total_price_label,
                    formatCurrency(itemCompleto.item.valorTotal)
                ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
