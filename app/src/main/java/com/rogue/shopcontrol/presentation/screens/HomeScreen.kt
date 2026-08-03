package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.components.ProductGastoRow
import com.rogue.shopcontrol.presentation.components.StatCard
import com.rogue.shopcontrol.presentation.viewmodel.HomeViewModel
import com.rogue.shopcontrol.utils.formatCurrency
import org.koin.androidx.compose.koinViewModel


@Composable
fun HomeScreen(
    onScannerClick: () -> Unit,
    onRecordsClick: () -> Unit,
    onSpendingCardClick: () -> Unit,
    onProductsClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = onScannerClick
            ) {

                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Scanner"
                )

            }

        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Button(
                onClick = onRecordsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text("Registros")

            }

            StatCard(
                title = "Valor gasto",
                value = formatCurrency(state.gastoMensal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onSpendingCardClick
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Produtos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = onProductsClick
                        ) {

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Ver todos os produtos"
                            )

                        }

                    }

                    state.topProdutos.forEach { produto ->

                        ProductGastoRow(
                            produto = produto
                        )

                    }

                }

            }

        }

    }

}
