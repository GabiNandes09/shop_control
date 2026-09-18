package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity

@Composable
fun CategoryPickerDialog(
    categorias: List<CategoriaEntity>,
    onCategorySelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    onCreateCategory: ((String) -> Unit)? = null
) {

    var busca by remember {
        mutableStateOf("")
    }

    val categoriasFiltradas =
        remember(categorias, busca) {

            if (busca.isBlank()) {
                categorias
            } else {
                categorias.filter { it.nome.contains(busca, ignoreCase = true) }
            }

        }

    val jaExiste =
        categorias.any { it.nome.equals(busca.trim(), ignoreCase = true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.select_category_title))
        },
        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = busca,
                    onValueChange = { busca = it },
                    label = {
                        Text(stringResource(R.string.search_by_name))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (onCreateCategory != null && busca.isNotBlank() && !jaExiste) {

                    Text(
                        text = stringResource(R.string.create_category_option, busca.trim()),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onCreateCategory(busca.trim())
                            }
                            .padding(vertical = 12.dp)
                    )

                }

                if (categoriasFiltradas.isEmpty()) {

                    Text(
                        text = stringResource(R.string.no_categories_available),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                    ) {

                        items(
                            items = categoriasFiltradas,
                            key = { it.id }
                        ) { categoria ->

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCategorySelected(categoria.id)
                                    }
                                    .padding(vertical = 12.dp)
                            ) {

                                Text(categoria.nome)

                            }

                        }

                    }

                }

            }

        },
        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(stringResource(R.string.cancel))

            }

        }
    )

}
