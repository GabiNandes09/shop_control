package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.data.local.entity.CategoriaEntity

@Composable
fun CategoryPickerDialog(
    categorias: List<CategoriaEntity>,
    onCategorySelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.select_category_title))
        },
        text = {

            if (categorias.isEmpty()) {

                Text(stringResource(R.string.no_categories_available))

            } else {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    categorias.forEach { categoria ->

                        Text(
                            text = categoria.nome,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(categoria.id)
                                }
                                .padding(vertical = 12.dp)
                        )

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
