package com.rogue.shopcontrol.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rogue.shopcontrol.presentation.screens.CategoryManagementScreen
import com.rogue.shopcontrol.presentation.screens.EstablishmentDetailScreen
import com.rogue.shopcontrol.presentation.screens.EstablishmentListScreen
import com.rogue.shopcontrol.presentation.screens.HomeScreen
import com.rogue.shopcontrol.presentation.screens.ProductCatalogScreen
import com.rogue.shopcontrol.presentation.screens.ProductDetailScreen
import com.rogue.shopcontrol.presentation.screens.ProductListScreen
import com.rogue.shopcontrol.presentation.screens.PurchaseDetailScreen
import com.rogue.shopcontrol.presentation.screens.RecordsScreen
import com.rogue.shopcontrol.presentation.screens.ScannerScreen
import com.rogue.shopcontrol.presentation.screens.SettingsScreen
import com.rogue.shopcontrol.presentation.screens.SpendingComparisonScreen


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
                },

                onSpendingCardClick = {
                    navController.navigate(
                        Routes.SpendingComparison.route
                    )
                },

                onProductsClick = {
                    navController.navigate(
                        Routes.Products.route
                    )
                },

                onSettingsClick = {
                    navController.navigate(
                        Routes.Settings.route
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
                onBackClick = {
                    navController.popBackStack()
                },
                onPurchaseClick = { compraId ->
                    navController.navigate(
                        Routes.PurchaseDetail.createRoute(compraId)
                    )
                }
            )

        }


        composable(Routes.SpendingComparison.route) {

            SpendingComparisonScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )

        }


        composable(Routes.Products.route) {

            ProductListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { produtoId ->
                    navController.navigate(
                        Routes.ProductDetail.createRoute(produtoId)
                    )
                }
            )

        }


        composable(Routes.Settings.route) {

            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductsClick = {
                    navController.navigate(
                        Routes.ProductCatalog.route
                    )
                },
                onCategoriesClick = {
                    navController.navigate(
                        Routes.CategoryManagement.route
                    )
                },
                onEstablishmentsClick = {
                    navController.navigate(
                        Routes.Establishments.route
                    )
                }
            )

        }


        composable(Routes.Establishments.route) {

            EstablishmentListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEstablishmentClick = { estabelecimentoId ->
                    navController.navigate(
                        Routes.EstablishmentDetail.createRoute(estabelecimentoId)
                    )
                }
            )

        }


        composable(
            route = Routes.EstablishmentDetail.route,
            arguments = listOf(
                navArgument("estabelecimentoId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val estabelecimentoId =
                backStackEntry.arguments
                    ?.getLong("estabelecimentoId")
                    ?: 0L

            EstablishmentDetailScreen(
                estabelecimentoId = estabelecimentoId,
                onBackClick = {
                    navController.popBackStack()
                },
                onPurchaseClick = { compraId ->
                    navController.navigate(
                        Routes.PurchaseDetail.createRoute(compraId)
                    )
                }
            )

        }


        composable(Routes.ProductCatalog.route) {

            ProductCatalogScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { produtoId ->
                    navController.navigate(
                        Routes.ProductDetail.createRoute(produtoId)
                    )
                }
            )

        }


        composable(Routes.CategoryManagement.route) {

            CategoryManagementScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )

        }


        composable(
            route = Routes.ProductDetail.route,
            arguments = listOf(
                navArgument("produtoId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val produtoId =
                backStackEntry.arguments
                    ?.getLong("produtoId")
                    ?: 0L

            ProductDetailScreen(
                produtoId = produtoId,
                onBackClick = {
                    navController.popBackStack()
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
                onBackClick = {
                    navController.popBackStack()
                },
                onDeleted = {
                    navController.popBackStack()
                }
            )

        }

    }

}