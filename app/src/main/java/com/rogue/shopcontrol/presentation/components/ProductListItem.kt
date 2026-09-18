package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ItemCompraCompleto
import com.rogue.shopcontrol.presentation.model.PriceAlert
import com.rogue.shopcontrol.utils.formatCurrency

private val PRICE_DOWN_COLOR = Color(0xFF4CAF50)
private val PRICE_UP_COLOR = Color(0xFFF44336)

@Composable
fun ProductListItem(
    itemCompleto: ItemCompraCompleto,
    onClick: () -> Unit,
    priceAlert: PriceAlert? = null,
    modifier: Modifier = Modifier
) {

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            EntityName(
                nome = itemCompleto.produto.nome,
                apelido = itemCompleto.produto.apelido,
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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        R.string.unit_price_label,
                        formatCurrency(itemCompleto.item.valorUnitario)
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )

                if (priceAlert != null) {

                    val cor =
                        if (priceAlert.maisCaro) PRICE_UP_COLOR else PRICE_DOWN_COLOR

                    Spacer(Modifier.width(8.dp))

                    Icon(
                        imageVector = if (priceAlert.maisCaro) {
                            Icons.Filled.ArrowUpward
                        } else {
                            Icons.Filled.ArrowDownward
                        },
                        contentDescription = stringResource(
                            if (priceAlert.maisCaro) {
                                R.string.price_increase_content_description
                            } else {
                                R.string.price_decrease_content_description
                            }
                        ),
                        tint = cor,
                        modifier = Modifier.height(16.dp)
                    )

                    Text(
                        text = formatCurrency(priceAlert.diferenca),
                        style = MaterialTheme.typography.bodySmall,
                        color = cor
                    )

                }

            }

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
