package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.EstabelecimentoEntity
import com.rogue.shopcontrol.utils.formatCurrency

@Composable
fun EstablishmentSummaryCard(
    estabelecimento: EstabelecimentoEntity,
    totalGasto: Double,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                EstablishmentName(
                    nome = estabelecimento.nome,
                    apelido = estabelecimento.apelido,
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(
                    onClick = onEditClick
                ) {

                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit_apelido_content_description)
                    )

                }

            }

            if (estabelecimento.cnpj.isNotBlank()) {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.cnpj_label, estabelecimento.cnpj),
                    style = MaterialTheme.typography.bodySmall
                )

            }

            if (estabelecimento.endereco.isNotBlank()) {

                Spacer(Modifier.height(4.dp))

                Text(
                    text = estabelecimento.endereco,
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.total_label, formatCurrency(totalGasto)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

        }

    }

}
