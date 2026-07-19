package com.rogue.shopcontrol.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rogue.shopcontrol.presentation.screens.HomeScreen
import com.rogue.shopcontrol.presentation.screens.RecordsScreen
import com.rogue.shopcontrol.presentation.screens.ScannerScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {


        composable(Routes.Home.route) {

            HomeScreen(
                onScannerClick = {
                    navController.navigate(
                        Routes.Scanner.route
                    )
                },

                onRecordsClick = {
                    navController.navigate(
                        Routes.Records.route
                    )
                }
            )

        }


        composable(
            Routes.Scanner.route
        ) {
            ScannerScreen { url ->
                Log.d(
                    "QR_CODE",
                    url
                )
            }
        }


        composable(Routes.Records.route) {

            RecordsScreen()

        }

    }

}