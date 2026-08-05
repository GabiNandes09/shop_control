package com.rogue.shopcontrol.presentation.navigation

sealed class Routes(
    val route: String
) {

    data object Home : Routes("home")

    data object Scanner : Routes("scanner")

    data object Records : Routes("records")

    data object SpendingComparison : Routes("spending_comparison")

    data object Products : Routes("products")

    data object Settings : Routes("settings")

    data object ProductCatalog : Routes("product_catalog")

    data object CategoryManagement : Routes("category_management")

    data object Establishments : Routes("establishments")

    data object PurchaseDetail : Routes("purchase_detail/{compraId}") {

        fun createRoute(compraId: Long) =
            "purchase_detail/$compraId"

    }

    data object ProductDetail : Routes("product_detail/{produtoId}") {

        fun createRoute(produtoId: Long) =
            "product_detail/$produtoId"

    }

    data object EstablishmentDetail : Routes("establishment_detail/{estabelecimentoId}") {

        fun createRoute(estabelecimentoId: Long) =
            "establishment_detail/$estabelecimentoId"

    }
}