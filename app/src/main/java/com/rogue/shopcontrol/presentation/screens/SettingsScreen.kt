package com.rogue.shopcontrol.presentation.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.ConfirmDialog
import com.rogue.shopcontrol.presentation.components.ScreenHeader
import com.rogue.shopcontrol.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onProductsClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onEstablishmentsClick: () -> Unit,
    onFontesClick: () -> Unit,
    onRendaCategoriasClick: () -> Unit,
    onComprasParceladasClick: () -> Unit,
    onARecebimentoClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val exportErrorMessage =
        stringResource(R.string.export_error)

    val importErrorMessage =
        stringResource(R.string.import_error)

    val importFilePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->

            uri?.let {
                viewModel.onImportFileSelected(it)
            }

        }

    LaunchedEffect(state.importResult) {

        state.importResult?.let { resultado ->

            Toast.makeText(
                context,
                context.getString(
                    R.string.import_success_message,
                    resultado.estabelecimentos,
                    resultado.produtos,
                    resultado.compras,
                    resultado.rendas
                ),
                Toast.LENGTH_LONG
            ).show()

            viewModel.onImportResultShown()

        }

    }

    LaunchedEffect(state.importErrorRes) {

        state.importErrorRes?.let {

            Toast.makeText(context, importErrorMessage, Toast.LENGTH_SHORT).show()

            viewModel.onImportErrorShown()

        }

    }

    LaunchedEffect(state.exportedFileUri) {

        state.exportedFileUri?.let { uri ->

            val intent =
                Intent(Intent.ACTION_SEND).apply {
                    type = "application/json"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

            context.startActivity(
                Intent.createChooser(intent, null)
            )

            viewModel.onExportHandled()

        }

    }

    LaunchedEffect(state.exportErrorRes) {

        state.exportErrorRes?.let {

            Toast.makeText(context, exportErrorMessage, Toast.LENGTH_SHORT).show()

            viewModel.onExportErrorShown()

        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.settings_title)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            SettingsSection(
                title = stringResource(R.string.settings_section_despesas)
            ) {

                Button(
                    onClick = onProductsClick,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(stringResource(R.string.products_section_title))

                }

                Button(
                    onClick = onCategoriesClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {

                    Text(stringResource(R.string.categories_button))

                }

                Button(
                    onClick = onEstablishmentsClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {

                    Text(stringResource(R.string.establishments_button))

                }

                Button(
                    onClick = onComprasParceladasClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {

                    Text(stringResource(R.string.compras_parceladas_title))

                }

            }

            SettingsSection(
                title = stringResource(R.string.settings_section_renda),
                modifier = Modifier.padding(top = 16.dp)
            ) {

                Button(
                    onClick = onFontesClick,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(stringResource(R.string.fontes_button))

                }

                Button(
                    onClick = onRendaCategoriasClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {

                    Text(stringResource(R.string.renda_categorias_button))

                }

                Button(
                    onClick = onARecebimentoClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {

                    Text(stringResource(R.string.arecebimento_title))

                }

            }

            SettingsSection(
                title = stringResource(R.string.settings_section_dados),
                modifier = Modifier.padding(top = 16.dp)
            ) {

                Button(
                    onClick = viewModel::onExportClick,
                    enabled = !state.isExporting,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        stringResource(
                            if (state.isExporting) {
                                R.string.export_data_in_progress
                            } else {
                                R.string.export_data_button
                            }
                        )
                    )

                }

                Button(
                    onClick = {
                        importFilePicker.launch(arrayOf("application/json"))
                    },
                    enabled = !state.isImporting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {

                    Text(
                        stringResource(
                            if (state.isImporting) {
                                R.string.import_data_in_progress
                            } else {
                                R.string.import_data_button
                            }
                        )
                    )

                }

            }

        }

    }

    if (state.showImportConfirm) {

        ConfirmDialog(
            title = stringResource(R.string.import_confirm_title),
            message = stringResource(R.string.import_confirm_message),
            confirmLabel = stringResource(R.string.import_data_button),
            onConfirm = viewModel::onConfirmImport,
            onDismiss = viewModel::onDismissImportConfirm
        )

    }

}


@Composable
private fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null
                )

            }

            if (expanded) {

                Column(
                    modifier = Modifier.padding(top = 12.dp)
                ) {

                    content()

                }

            }

        }

    }

}
