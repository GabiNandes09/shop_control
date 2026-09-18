package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.ProdutoEntity

@Composable
fun LinkProdutosToCategoriaDialog(
    titulo: String,
    produtos: List<ProdutoEntity>,
    selecionados: Set<Long>,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onToggleProduto: (Long) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {

    val produtosFiltrados =
        remember(produtos, searchQuery) {

            if (searchQuery.isBlank()) {
                produtos
            } else {
                produtos.filter { it.nome.contains(searchQuery, ignoreCase = true) }
            }

        }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(titulo)
        },
        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    label = {
                        Text(stringResource(R.string.search_by_name))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(
                        R.string.link_produtos_selecionados_count,
                        selecionados.size
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {

                    items(
                        items = produtosFiltrados,
                        key = { it.id }
                    ) { produto ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = produto.id in selecionados,
                                onCheckedChange = { onToggleProduto(produto.id) }
                            )

                            Text(
                                text = produto.nome,
                                modifier = Modifier.weight(1f)
                            )

                        }

                    }

                }

            }

        },
        confirmButton = {

            TextButton(
                onClick = onSave
            ) {

                Text(stringResource(R.string.link_produtos_save_button))

            }

        },
        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text(stringResource(R.string.cancel))

            }

        }
    )

}
