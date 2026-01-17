package com.example.test_anik.presentation.navigation

sealed class ToLetScreen(val route: String) {
    object ToLetList : ToLetScreen("to_let_list")
    object ToLetDetails : ToLetScreen("to_let_details/{propertyId}") {
        fun createRoute(propertyId: Int) = "to_let_details/$propertyId"
    }
    object AddToLet : ToLetScreen("add_tolet")
}