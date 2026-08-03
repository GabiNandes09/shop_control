package com.rogue.shopcontrol.presentation.navigation

sealed class Routes(
    val route: String
) {

    data object Home : Routes("home")

    data object Scanner : Routes("scanner")

    data object Records : Routes("records")

    data object SpendingComparison : Routes("spending_comparison")

    data object Products : Routes("products")

    data object PurchaseDetail : Routes("purchase_detail/{compraId}") {

        fun createRoute(compraId: Long) =
            "purchase_detail/$compraId"

    }

    data object ProductDetail : Routes("product_detail/{produtoId}") {

        fun createRoute(produtoId: Long) =
            "product_detail/$produtoId"

    }
}