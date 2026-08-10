package com.rogue.shopcontrol.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.presentation.screens.CategoryManagementScreen
import com.rogue.shopcontrol.presentation.screens.CategorySpendingScreen
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

private val BOTTOM_BAR_ROUTES =
    setOf(
        Routes.Home.route,
        Routes.Records.route,
        Routes.Settings.route
    )

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val currentBackStackEntry by
        navController.currentBackStackEntryAsState()

    val currentRoute =
        currentBackStackEntry?.destination?.route


    Scaffold(
        bottomBar = {

            if (currentRoute in BOTTOM_BAR_ROUTES) {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentRoute == Routes.Home.route,
                        onClick = {

                            navController.navigate(Routes.Home.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true

                            }

                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                stringResource(R.string.home_tab_label)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Records.route,
                        onClick = {

                            navController.navigate(Routes.Records.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true

                            }

                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                stringResource(R.string.records_button)
                            )
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.Settings.route,
                        onClick = {

                            navController.navigate(Routes.Settings.route) {

                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true

                            }

                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                stringResource(R.string.settings_title)
                            )
                        }
                    )

                }

            }

        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Routes.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {


            composable(Routes.Home.route) {

                HomeScreen(
                    onScannerClick = {
                        navController.navigate(
                            Routes.Scanner.route
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


            composable(Routes.CategorySpending.route) {

                CategorySpendingScreen(
                    onBackClick = {
                        navController.popBackStack()
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
                    },
                    onCategorySpendingClick = {
                        navController.navigate(
                            Routes.CategorySpending.route
                        )
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

}
