package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R

@Composable
fun CategoryListItem(
    nome: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    grupoNome: String? = null,
    onGrupoClick: (() -> Unit)? = null,
    onLinkProdutosClick: (() -> Unit)? = null
) {

    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = nome,
                    style = MaterialTheme.typography.bodyMedium
                )

                grupoNome?.let {

                    Text(
                        text = stringResource(R.string.categoria_grupo_label, it),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                }

            }

            if (onLinkProdutosClick != null) {

                IconButton(
                    onClick = onLinkProdutosClick
                ) {

                    Icon(
                        imageVector = Icons.Filled.Link,
                        contentDescription = stringResource(R.string.link_produtos_content_description)
                    )

                }

            }

            if (onGrupoClick != null) {

                IconButton(
                    onClick = onGrupoClick
                ) {

                    Icon(
                        imageVector = Icons.Filled.Category,
                        contentDescription = stringResource(R.string.categoria_grupo_content_description)
                    )

                }

            }

            IconButton(
                onClick = onEditClick
            ) {

                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_category_content_description)
                )

            }

            IconButton(
                onClick = onDeleteClick
            ) {

                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_category_content_description)
                )

            }

        }

    }

}
