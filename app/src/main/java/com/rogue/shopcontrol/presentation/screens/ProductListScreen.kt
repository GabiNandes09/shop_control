package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.ProductGastoRow
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.ProductListViewModel
import com.rogue.shopcontrol.presentation.viewmodel.states.ProdutoSortOption
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onBackClick: () -> Unit,
    onProductClick: (Long) -> Unit,
    viewModel: ProductListViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.all_products_title),
            onBackClick = onBackClick
        )

        FilterToggleChip(
            expanded = filtersExpanded,
            onClick = { filtersExpanded = !filtersExpanded },
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

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

                state.produtos.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(stringResource(R.string.products_empty))

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

            FilterOverlay(
                expanded = filtersExpanded,
                onDismiss = { filtersExpanded = false }
            ) {

                DateRangeSelector(
                    dateRange = state.dateRange,
                    onDateRangeChanged = viewModel::onDateRangeChanged
                )

                OutlinedTextField(
                    value = state.nameFilter,
                    onValueChange = viewModel::onNameFilterChanged,
                    label = {
                        Text(stringResource(R.string.search_by_name))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                var sortExpanded by remember {
                    mutableStateOf(false)
                }

                ExposedDropdownMenuBox(
                    expanded = sortExpanded,
                    onExpandedChange = { sortExpanded = it }
                ) {

                    OutlinedTextField(
                        value = stringResource(state.sortOption.labelRes),
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(stringResource(R.string.sort_by))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = sortExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false }
                    ) {

                        ProdutoSortOption.entries.forEach { option ->

                            DropdownMenuItem(
                                text = {
                                    Text(stringResource(option.labelRes))
                                },
                                onClick = {
                                    viewModel.onSortOptionSelected(option)
                                    sortExpanded = false
                                }
                            )

                        }

                    }

                }

                var categoryExpanded by remember {
                    mutableStateOf(false)
                }

                val todasCategoriasLabel =
                    stringResource(R.string.all_categories_option)

                val selectedCategoryName =
                    state.categorias
                        .firstOrNull { it.id == state.selectedCategoryId }
                        ?.nome
                        ?: todasCategoriasLabel

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it }
                ) {

                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text(stringResource(R.string.category_filter_label))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = categoryExpanded
                            )
                        },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text(todasCategoriasLabel)
                            },
                            onClick = {
                                viewModel.onCategoryFilterSelected(null)
                                categoryExpanded = false
                            }
                        )

                        state.categorias.forEach { categoria ->

                            DropdownMenuItem(
                                text = {
                                    Text(categoria.nome)
                                },
                                onClick = {
                                    viewModel.onCategoryFilterSelected(categoria.id)
                                    categoryExpanded = false
                                }
                            )

                        }

                    }

                }

            }

        }

    }

}
