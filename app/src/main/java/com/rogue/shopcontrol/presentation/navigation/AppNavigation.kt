package com.rogue.shopcontrol.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rogue.shopcontrol.presentation.screens.HomeScreen
import com.rogue.shopcontrol.presentation.screens.PurchaseDetailScreen
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

            ScannerScreen(
                onPurchaseSaved = { compraId ->
                    navController.navigate(
                        Routes.PurchaseDetail.createRoute(compraId)
                    ) {
                        popUpTo(Routes.Scanner.route) {
                            inclusive = true
                        }
                    }
                }
            )

        }


        composable(Routes.Records.route) {

            RecordsScreen(
                onPurchaseClick = { compraId ->
                    navController.navigate(
                        Routes.PurchaseDetail.createRoute(compraId)
                    )
                }
            )

        }


        composable(
            route = Routes.PurchaseDetail.route,
            arguments = listOf(
                navArgument("compraId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val compraId =
                backStackEntry.arguments
                    ?.getLong("compraId")
                    ?: 0L

            PurchaseDetailScreen(
                compraId = compraId,
                onDeleted = {
                    navController.popBackStack()
                }
            )

        }

    }

}