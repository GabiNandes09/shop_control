package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.CategoryPickerDialog
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.EanInputDialog
import com.rogue.shopcontrol.presentation.components.ProductHistoryRow
import com.rogue.shopcontrol.presentation.components.ProductPickerDialog
import com.rogue.shopcontrol.presentation.components.ProductSummaryCard
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.components.TextInputDialog
import com.rogue.shopcontrol.presentation.viewmodel.ProductDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailScreen(
    produtoId: Long,
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel(
        parameters = { parametersOf(produtoId) }
    )
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isMerged) {

        if (state.isMerged) {
            onBackClick()
        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.product_detail_title),
            onBackClick = onBackClick,
            actions = {

                var showMenu by remember {
                    mutableStateOf(false)
                }

                IconButton(
                    onClick = { showMenu = true }
                ) {

                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.product_options_content_description)
                    )

                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.merge_product_button))
                        },
                        onClick = {
                            showMenu = false
                            viewModel.onShowMergePicker()
                        }
                    )

                }

            }
        )

        val produto = state.produto

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

            produto == null -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(stringResource(R.string.product_not_found))

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
                            produto = produto,
                            highestPrice = state.highestPrice,
                            lowestPrice = state.lowestPrice,
                            onAddCategoryClick = viewModel::onAddCategoryClick,
                            onEditEanClick = viewModel::onEditEanClick,
                            onEditApelidoClick = viewModel::onEditApelidoClick
                        )

                    }

                    item {

                        Text(
                            text = stringResource(R.string.purchase_history_title),
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

    if (state.showCategoryPicker) {

        CategoryPickerDialog(
            categorias = state.categorias,
            onCategorySelected = viewModel::onCategorySelected,
            onDismiss = viewModel::onCategoryPickerDismiss,
            onCreateCategory = viewModel::onCreateCategory
        )

    }

    if (state.showEanDialog) {

        EanInputDialog(
            value = state.eanInput,
            onValueChange = viewModel::onEanInputChanged,
            onSave = viewModel::onSaveEan,
            onDismiss = viewModel::onEanDialogDismiss
        )

    }

    if (state.showApelidoDialog) {

        TextInputDialog(
            title = stringResource(R.string.edit_produto_apelido_title),
            label = stringResource(R.string.apelido_label),
            value = state.apelidoInput,
            onValueChange = viewModel::onApelidoInputChanged,
            onSave = viewModel::onSaveApelido,
            onDismiss = viewModel::onApelidoDialogDismiss
        )

    }

    if (state.showMergePicker) {

        ProductPickerDialog(
            produtos = state.todosProdutos,
            onProductSelected = viewModel::onMergeTargetSelected,
            onDismiss = viewModel::onDismissMergePicker
        )

    }

    if (state.showMergeConfirm) {

        ConfirmDialog(
            title = stringResource(R.string.merge_product_confirm_title),
            message = stringResource(R.string.merge_product_confirm_message),
            onConfirm = viewModel::onConfirmMerge,
            onDismiss = viewModel::onDismissMergeConfirm
        )

    }

}
