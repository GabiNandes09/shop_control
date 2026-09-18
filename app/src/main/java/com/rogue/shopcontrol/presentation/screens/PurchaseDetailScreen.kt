package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.OrigemCompra
import com.rogue.shopcontrol.data.local.entity.TipoCompra
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.ParcelaRow
import com.rogue.shopcontrol.presentation.components.ProductListItem
import com.rogue.shopcontrol.presentation.components.PurchaseSummaryCard
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.PurchaseDetailViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PurchaseDetailScreen(
    compraId: Long,
    onBackClick: () -> Unit,
    onDeleted: () -> Unit,
    onEditClick: (Long) -> Unit,
    onProductClick: (Long) -> Unit,
    viewModel: PurchaseDetailViewModel = koinViewModel(
        parameters = { parametersOf(compraId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDeleteConfirm by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.isDeleted) {

        if (state.isDeleted) {
            onDeleted()
        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(onBackClick = onBackClick)

        val compra = state.compra

        when {

            state.isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(R.string.loading))

                }

            }

            compra == null -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(R.string.purchase_not_found))

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    if (compra.compra.origem == OrigemCompra.MANUAL) {

                        item {

                            Button(
                                onClick = {
                                    onEditClick(compraId)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Text(stringResource(R.string.edit_purchase))

                            }

                        }

                    }

                    item {

                        PurchaseSummaryCard(
                            compra = compra
                        )

                    }

                    if (compra.compra.tipo == TipoCompra.VARIAVEL) {

                        state.categoriaNome?.let { categoriaNome ->

                            item {

                                Text(
                                    text = stringResource(R.string.category_label, categoriaNome),
                                    style = MaterialTheme.typography.bodyMedium
                                )

                            }

                        }

                        items(
                            items = compra.itens,
                            key = { it.item.id }
                        ) { itemCompleto ->

                            ProductListItem(
                                itemCompleto = itemCompleto,
                                onClick = {
                                    onProductClick(itemCompleto.produto.id)
                                },
                                priceAlert = state.precoAlerts[itemCompleto.item.id]
                            )

                        }

                    } else {

                        item {

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = compra.compra.nome ?: "",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    state.categoriaNome?.let { categoriaNome ->

                                        Text(
                                            text = stringResource(R.string.category_label, categoriaNome),
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                    }

                                    Text(
                                        text = formatCurrency(compra.compra.valorTotal),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                }

                            }

                        }

                        if (compra.compra.tipo == TipoCompra.PARCELADA && state.parcelas.isNotEmpty()) {

                            item {

                                Text(
                                    text = stringResource(R.string.parcelas_section_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                            }

                            items(
                                items = state.parcelas,
                                key = { it.id }
                            ) { parcela ->

                                ParcelaRow(
                                    parcela = parcela,
                                    numero = state.parcelas.indexOf(parcela) + 1,
                                    isAtual = parcela.id == compra.compra.id
                                )

                            }

                        }

                    }

                    if (compra.compra.dividida && state.divisaoParticipantes.isNotEmpty()) {

                        item {

                            Text(
                                text = stringResource(R.string.divisao_participantes_title),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                        }

                        items(
                            items = state.divisaoParticipantes,
                            key = { it.fonteRendaId }
                        ) { participante ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(text = participante.fonteNome)

                                Text(text = formatCurrency(participante.valor))

                            }

                        }

                    }

                    item {

                        Button(
                            onClick = {
                                showDeleteConfirm = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {

                            Text(stringResource(R.string.delete_purchase))

                        }

                    }

                }

            }

        }

    }

    if (showDeleteConfirm) {

        ConfirmDialog(
            title = stringResource(R.string.delete_purchase),
            message = stringResource(
                if (state.compra?.compra?.tipo == TipoCompra.FIXA ||
                    state.compra?.compra?.tipo == TipoCompra.PARCELADA
                ) {
                    R.string.delete_compra_recorrente_confirm_message
                } else {
                    R.string.delete_purchase_confirm_message
                }
            ),
            confirmLabel = stringResource(R.string.delete),
            onConfirm = {
                showDeleteConfirm = false
                viewModel.delete()
            },
            onDismiss = {
                showDeleteConfirm = false
            }
        )

    }

}
