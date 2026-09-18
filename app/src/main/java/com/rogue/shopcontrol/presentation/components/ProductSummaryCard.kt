package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun ProductSummaryCard(
    produto: ProdutoGasto,
    highestPrice: Double?,
    lowestPrice: Double?,
    onAddCategoryClick: () -> Unit,
    onEditEanClick: () -> Unit,
    onEditApelidoClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val precoMedio =
        if (produto.quantidadeTotal > 0) {
            produto.valorTotalGasto / produto.quantidadeTotal
        } else {
            0.0
        }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            EntityName(
                nome = produto.nome,
                apelido = produto.apelido,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        R.string.category_label,
                        produto.categoriaNome ?: stringResource(R.string.no_category)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onAddCategoryClick
                ) {

                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_category_content_description)
                    )

                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        R.string.ean_field_label,
                        produto.codigoBarras ?: stringResource(R.string.ean_not_informed)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onEditEanClick
                ) {

                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.edit_ean_content_description)
                    )

                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(
                        R.string.apelido_label
                    ) + ": " + (produto.apelido?.takeIf { it.isNotBlank() } ?: stringResource(R.string.no_apelido_informed)),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onEditApelidoClick
                ) {

                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.edit_produto_apelido_content_description)
                    )

                }

            }

            Text(
                text = stringResource(R.string.quantity_total_label, produto.quantidadeTotal),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.average_price_label, formatCurrency(precoMedio)),
                style = MaterialTheme.typography.bodyMedium
            )

            highestPrice?.let {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.highest_price_label, formatCurrency(it)),
                    style = MaterialTheme.typography.bodyMedium
                )

            }

            lowestPrice?.let {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.lowest_price_label, formatCurrency(it)),
                    style = MaterialTheme.typography.bodyMedium
                )

            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(
                    R.string.total_spent_label,
                    formatCurrency(produto.valorTotalGasto)
                ),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
