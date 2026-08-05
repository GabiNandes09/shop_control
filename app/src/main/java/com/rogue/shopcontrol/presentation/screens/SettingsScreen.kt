package com.rogue.shopcontrol.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.components.ScreenHeader

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onProductsClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onEstablishmentsClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenHeader(
            title = stringResource(R.string.settings_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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

        }

    }

}
