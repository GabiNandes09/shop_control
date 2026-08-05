package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun EstablishmentName(
    nome: String,
    apelido: String?,
    style: TextStyle,
    modifier: Modifier = Modifier
) {

    val temApelido = !apelido.isNullOrBlank()

    Column(
        modifier = modifier
    ) {

        Text(
            text = if (temApelido) apelido else nome,
            style = style
        )

        if (temApelido) {

            Text(
                text = nome,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }

    }

}
