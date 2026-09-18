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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import com.rogue.shopcontrol.presentation.components.CategoryListItem
import com.rogue.shopcontrol.presentation.components.CategoryPickerDialog
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.LinkProdutosToCategoriaDialog
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.components.TextInputDialog
import com.rogue.shopcontrol.presentation.viewmodel.CategoryManagementViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategoryManagementScreen(
    onBackClick: () -> Unit,
    onCategorySpendingClick: () -> Unit,
    viewModel: CategoryManagementViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    var filtersExpanded by remember {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = viewModel::onShowAddDialog
            ) {

                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_category_content_description)
                )

            }

        }
    ) { innerPadding ->

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        ScreenHeader(
            title = stringResource(R.string.categories_button),
            onBackClick = onBackClick
        )

        Button(
            onClick = onCategorySpendingClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                )
        ) {

            Text(stringResource(R.string.category_spending_title))

        }

        state.errorRes?.let { errorRes ->

            Text(
                text = stringResource(errorRes),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )

        }

        HorizontalDivider()

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

                state.categorias.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(stringResource(R.string.no_categories_available))

                    }

                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = state.categorias,
                            key = { it.id }
                        ) { categoria ->

                            CategoryListItem(
                                nome = categoria.nome,
                                grupoNome = state.grupoNomeDe(categoria),
                                onEditClick = {
                                    viewModel.onEditClick(categoria)
                                },
                                onDeleteClick = {
                                    viewModel.onDeleteCategory(categoria.id)
                                },
                                onGrupoClick = {
                                    viewModel.onShowGrupoPicker(categoria)
                                },
                                onLinkProdutosClick = {
                                    viewModel.onShowLinkProdutosDialog(categoria)
                                }
                            )

                        }

                    }

                }

            }

            FilterOverlay(
                expanded = filtersExpanded,
                onDismiss = { filtersExpanded = false }
            ) {

                OutlinedTextField(
                    value = state.nameFilter,
                    onValueChange = viewModel::onNameFilterChanged,
                    label = {
                        Text(stringResource(R.string.search_by_name))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

            }

        }

    }

    }

    if (state.showAddDialog) {

        TextInputDialog(
            title = stringResource(R.string.add_category_dialog_title),
            label = stringResource(R.string.category_name_label),
            value = state.newCategoryName,
            onValueChange = viewModel::onNewCategoryNameChanged,
            onSave = viewModel::onAddCategory,
            onDismiss = viewModel::onDismissAddDialog
        )

    }

    state.editingCategoria?.let {

        TextInputDialog(
            title = stringResource(R.string.edit_category_title),
            label = stringResource(R.string.category_name_label),
            value = state.editCategoryName,
            onValueChange = viewModel::onEditCategoryNameChanged,
            onSave = viewModel::onSaveEdit,
            onDismiss = viewModel::onEditDismiss
        )

    }

    if (state.showGrupoPicker) {

        val categoriaAtual = state.categoriaParaGrupo

        CategoryPickerDialog(
            categorias = state.todasCategorias.filter { it.id != categoriaAtual?.id },
            onCategorySelected = viewModel::onGrupoSelected,
            onDismiss = viewModel::onDismissGrupoPicker
        )

    }

    if (state.showLinkProdutosDialog) {

        val categoria = state.categoriaParaLink

        if (categoria != null) {

            LinkProdutosToCategoriaDialog(
                titulo = stringResource(R.string.link_produtos_title),
                produtos = state.todosProdutos,
                selecionados = state.produtosSelecionados,
                searchQuery = state.produtoSearchQuery,
                onSearchQueryChanged = viewModel::onProdutoSearchChanged,
                onToggleProduto = viewModel::onToggleProdutoSelecionado,
                onSave = viewModel::onSaveLinkProdutos,
                onDismiss = viewModel::onDismissLinkProdutosDialog
            )

        }

    }

}
