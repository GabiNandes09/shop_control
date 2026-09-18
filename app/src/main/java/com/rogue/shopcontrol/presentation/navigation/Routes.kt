package com.rogue.shopcontrol.presentation.navigation

sealed class Routes(
    val route: String
) {

    data object Home : Routes("home")

    data object Analise : Routes("analise")

    data object Scanner : Routes("scanner")

    data object Records : Routes("records")

    data object SpendingComparison : Routes("spending_comparison")

    data object Products : Routes("products")

    data object Settings : Routes("settings")

    data object ProductCatalog : Routes("product_catalog")

    data object CategoryManagement : Routes("category_management")

    data object Establishments : Routes("establishments")

    data object CategorySpending : Routes("category_spending")

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

    data object ManualPurchaseEntry : Routes("manual_purchase_entry?compraId={compraId}") {

        fun createRoute(compraId: Long = 0L) =
            "manual_purchase_entry?compraId=$compraId"

    }

    data object Renda : Routes("renda")

    data object Fontes : Routes("fontes")

    data object RendaCategoriaManagement : Routes("renda_categoria_management")

    data object FonteDetail : Routes("fonte_detail/{fonteId}") {

        fun createRoute(fonteId: Long) =
            "fonte_detail/$fonteId"

    }

    data object ComprasParceladas : Routes("compras_parceladas")

    data object Balanco : Routes("balanco")

    data object ARecebimento : Routes("arecebimento?highlightId={highlightId}") {

        fun createRoute(highlightId: Long = 0L) =
            "arecebimento?highlightId=$highlightId"

    }
}