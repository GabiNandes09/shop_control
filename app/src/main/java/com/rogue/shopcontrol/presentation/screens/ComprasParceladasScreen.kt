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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.ComprasParceladasViewModel
import com.rogue.shopcontrol.presentation.viewmodel.states.SerieParcelada
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel

@Composable
fun ComprasParceladasScreen(
    onBackClick: () -> Unit,
    onCompraClick: (Long) -> Unit,
    viewModel: ComprasParceladasViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.compras_parceladas_title),
            onBackClick = onBackClick
        )

        if (state.isLoading) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(stringResource(R.string.loading))

            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = stringResource(R.string.compras_parceladas_valor_mensal_label),
                                style = MaterialTheme.typography.labelMedium
                            )

                            Text(
                                text = formatCurrency(state.valorMensalComprometido),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                        }

                    }

                }

                if (state.ativas.isEmpty() && state.concluidas.isEmpty()) {

                    item {

                        Text(stringResource(R.string.compras_parceladas_empty))

                    }

                }

                items(
                    items = state.ativas,
                    key = { it.compraIdAtual }
                ) { serie ->

                    SerieParceladaCard(
                        serie = serie,
                        onClick = {
                            onCompraClick(serie.compraIdAtual)
                        }
                    )

                }

                if (state.concluidas.isNotEmpty()) {

                    item {

                        TextButton(
                            onClick = viewModel::onToggleConcluidas
                        ) {

                            Text(
                                stringResource(
                                    if (state.mostrarConcluidas) {
                                        R.string.compras_parceladas_ocultar_concluidas
                                    } else {
                                        R.string.compras_parceladas_mostrar_concluidas
                                    }
                                )
                            )

                        }

                    }

                    if (state.mostrarConcluidas) {

                        items(
                            items = state.concluidas,
                            key = { it.compraIdAtual }
                        ) { serie ->

                            SerieParceladaCard(
                                serie = serie,
                                onClick = {
                                    onCompraClick(serie.compraIdAtual)
                                }
                            )

                        }

                    }

                }

            }

        }

    }

}


@Composable
private fun SerieParceladaCard(
    serie: SerieParcelada,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = serie.nome,
                style = MaterialTheme.typography.titleMedium
            )

            serie.categoriaNome?.let {

                Text(
                    text = stringResource(R.string.category_label, it),
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = formatCurrency(serie.valorParcela),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(
                        R.string.compras_parceladas_progresso_label,
                        serie.parcelaAtualNumero,
                        serie.totalParcelas
                    )
                )

            }

            LinearProgressIndicator(
                progress = { serie.parcelaAtualNumero.toFloat() / serie.totalParcelas.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )

        }

    }

}
