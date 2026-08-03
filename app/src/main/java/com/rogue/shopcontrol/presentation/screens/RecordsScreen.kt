package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.components.PurchaseListItem
import com.rogue.shopcontrol.presentation.components.RecordsHeader
import com.rogue.shopcontrol.presentation.viewmodel.RecordsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordsScreen(
    viewModel: RecordsViewModel = koinViewModel(),
    onPurchaseClick: (Long) -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        RecordsHeader()

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

            state.compras.isEmpty() -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Nenhuma compra registrada")

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = state.compras,
                        key = { it.compra.id }
                    ) { compraCompleta ->

                        PurchaseListItem(
                            compra = compraCompleta,
                            onClick = {
                                onPurchaseClick(compraCompleta.compra.id)
                            }
                        )

                    }

                }

            }

        }

    }

}
