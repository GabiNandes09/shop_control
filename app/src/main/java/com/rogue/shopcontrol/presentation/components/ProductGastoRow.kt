package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ProdutoGasto
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun ProductGastoRow(
    produto: ProdutoGasto,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            EntityName(
                nome = produto.nome,
                apelido = produto.apelido,
                style = MaterialTheme.typography.bodyMedium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = stringResource(R.string.product_qty_short, produto.quantidadeTotal),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ProdutoCadastroIcons(
                    temEan = !produto.codigoBarras.isNullOrBlank(),
                    temCategoria = produto.categoriaId != null && produto.categoriaId != 0L,
                    modifier = Modifier.padding(start = 8.dp)
                )

            }

        }

        Text(
            text = formatCurrency(produto.valorTotalGasto),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

    }

}


@Composable
fun ProdutoCadastroIcons(
    temEan: Boolean,
    temCategoria: Boolean,
    modifier: Modifier = Modifier
) {

    val corAtivo = MaterialTheme.colorScheme.primary
    val corInativo = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Filled.QrCode,
            contentDescription = stringResource(
                if (temEan) R.string.produto_tem_ean_content_description else R.string.produto_sem_ean_content_description
            ),
            tint = if (temEan) corAtivo else corInativo,
            modifier = Modifier.width(16.dp)
        )

        Row(modifier = Modifier.width(4.dp)) {}

        Icon(
            imageVector = Icons.AutoMirrored.Filled.Label,
            contentDescription = stringResource(
                if (temCategoria) R.string.produto_tem_categoria_content_description else R.string.produto_sem_categoria_content_description
            ),
            tint = if (temCategoria) corAtivo else corInativo,
            modifier = Modifier.width(16.dp)
        )

    }

}
