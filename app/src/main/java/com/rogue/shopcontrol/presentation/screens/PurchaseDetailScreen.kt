package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.ProductListItem
import com.rogue.shopcontrol.presentation.components.PurchaseSummaryCard
import com.rogue.shopcontrol.presentation.components.RecordsHeader
import com.rogue.shopcontrol.presentation.viewmodel.PurchaseDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PurchaseDetailScreen(
    compraId: Long,
    onDeleted: () -> Unit,
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

        RecordsHeader()

        val compra = state.compra

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

            compra == null -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Compra não encontrada")

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {

                        PurchaseSummaryCard(
                            compra = compra
                        )

                    }

                    items(
                        items = compra.itens,
                        key = { it.item.id }
                    ) { itemCompleto ->

                        ProductListItem(
                            itemCompleto = itemCompleto
                        )

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

                            Text("Excluir compra")

                        }

                    }

                }

            }

        }

    }

    if (showDeleteConfirm) {

        ConfirmDialog(
            title = "Excluir compra",
            message = "Essa ação não pode ser desfeita. Deseja realmente excluir esta compra?",
            confirmLabel = "Excluir",
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
