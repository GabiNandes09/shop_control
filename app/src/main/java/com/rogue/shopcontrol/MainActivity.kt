package com.rogue.shopcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rogue.shopcontrol.presentation.navigation.AppNavigation
import com.rogue.shopcontrol.ui.theme.ShopControlTheme

class MainActivity : ComponentActivity() {


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)


        setContent {


            ShopControlTheme {


                AppNavigation()

            }

        }

    }
}