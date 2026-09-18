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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CompraCompleta
import com.rogue.shopcontrol.data.local.entity.TipoCompra
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

            EntityName(
                nome = compra.estabelecimento.nome,
                apelido = compra.estabelecimento.apelido,
                style = MaterialTheme.typography.titleMedium
            )

            if (compra.compra.tipo != TipoCompra.VARIAVEL) {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = compra.compra.nome ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = compra.compra.dataCompra ?: stringResource(R.string.data_not_informed),
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
