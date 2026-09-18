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
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.rogue.shopcontrol.presentation.components.AddEstablishmentDialog
import com.rogue.shopcontrol.presentation.components.CategoryPickerDialog
import com.rogue.shopcontrol.presentation.components.DateRangeSelector
import com.rogue.shopcontrol.presentation.components.EstablishmentGastoRow
import com.rogue.shopcontrol.presentation.components.FilterOverlay
import com.rogue.shopcontrol.presentation.components.FilterToggleChip
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.EstablishmentListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EstablishmentListScreen(
    onBackClick: () -> Unit,
    onEstablishmentClick: (Long) -> Unit,
    viewModel: EstablishmentListViewModel = koinViewModel()
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
                    contentDescription = stringResource(R.string.add_establishment_content_description)
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
            title = stringResource(R.string.establishments_button),
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

                state.estabelecimentos.isEmpty() -> {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(stringResource(R.string.establishments_empty))

                    }

                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = state.estabelecimentos,
                            key = { it.id }
                        ) { estabelecimento ->

                            Card(
                                onClick = {
                                    onEstablishmentClick(estabelecimento.id)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                EstablishmentGastoRow(
                                    estabelecimento = estabelecimento,
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

            }

        }

    }

    }

    if (state.showAddDialog) {

        AddEstablishmentDialog(
            nome = state.newNome,
            onNomeChanged = viewModel::onNewNomeChanged,
            cnpj = state.newCnpj,
            onCnpjChanged = viewModel::onNewCnpjChanged,
            endereco = state.newEndereco,
            onEnderecoChanged = viewModel::onNewEnderecoChanged,
            apelido = state.newApelido,
            onApelidoChanged = viewModel::onNewApelidoChanged,
            categorias = state.categorias,
            categoriaSelecionadaId = state.newCategoriaId,
            onShowCategoriaPicker = viewModel::onShowNewCategoryPicker,
            errorRes = state.addErrorRes,
            onSave = viewModel::onSaveNewEstabelecimento,
            onDismiss = viewModel::onDismissAddDialog
        )

    }

    if (state.showNewCategoryPicker) {

        CategoryPickerDialog(
            categorias = state.categorias,
            onCategorySelected = viewModel::onNewCategoriaSelecionada,
            onDismiss = viewModel::onDismissNewCategoryPicker,
            onCreateCategory = viewModel::onCreateNewCategoria
        )

    }

}
