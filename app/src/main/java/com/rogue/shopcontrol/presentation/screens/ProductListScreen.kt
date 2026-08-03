package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.presentation.components.ProductGastoRow
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.ProductListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.states.ProdutoSortOption
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Long) -> Unit,
    viewModel: ProductListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader("Todos os Produtos")

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = state.nameFilter,
                onValueChange = viewModel::onNameFilterChanged,
                label = {
                    Text("Buscar por nome")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            var expanded by remember {
                mutableStateOf(false)
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {

                OutlinedTextField(
                    value = state.sortOption.label,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Ordenar por")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {

                    ProdutoSortOption.entries.forEach { option ->

                        DropdownMenuItem(
                            text = {
                                Text(option.label)
                            },
                            onClick = {
                                viewModel.onSortOptionSelected(option)
                                expanded = false
                            }
                        )

                    }

                }

            }

        }

        HorizontalDivider()

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

            state.produtos.isEmpty() -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Nenhum produto encontrado")

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(
                        items = state.produtos,
                        key = { it.id }
                    ) { produto ->

                        Card(
                            onClick = {
                                onProductClick(produto.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            ProductGastoRow(
                                produto = produto,
                                modifier = Modifier.padding(16.dp)
                            )

                        }

                    }

                }

            }

        }

    }

}
