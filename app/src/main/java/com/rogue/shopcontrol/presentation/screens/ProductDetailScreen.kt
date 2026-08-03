package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.components.ProductHistoryRow
import com.rogue.shopcontrol.presentation.components.ProductSummaryCard
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailScreen(
    produtoId: Long,
    viewModel: ProductDetailViewModel = koinViewModel(
        parameters = { parametersOf(produtoId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader("Detalhes do Produto")

        val produto = state.produto

        when {

            state.isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Carregando...")

                }

            }

            produto == null -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Produto não encontrado")

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {

                        ProductSummaryCard(
                            produto = produto
                        )

                    }

                    item {

                        Text(
                            text = "Histórico de compras",
                            style = MaterialTheme.typography.titleMedium
                        )

                    }

                    items(
                        items = state.historico
                    ) { historico ->

                        ProductHistoryRow(
                            historico = historico
                        )

                    }

                }

            }

        }

    }

}
