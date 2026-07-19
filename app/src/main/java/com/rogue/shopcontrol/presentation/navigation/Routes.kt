package com.rogue.shopcontrol.presentation.navigation

sealed class Routes(
    val route: String
) {

    data object Home : Routes("home")

    data object Scanner : Routes("scanner")

    data object Records : Routes("records")
}