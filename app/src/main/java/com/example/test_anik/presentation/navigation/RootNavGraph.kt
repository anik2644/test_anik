package com.example.test_anik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test_anik.presentation.screens.MainScreen
import com.example.test_anik.presentation.screens.ModernLoginScreen1

@Composable
fun RootNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            ModernLoginScreen1(
                onLoginClick = { _, _ ->
                    navController.navigate("main") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "main") {
            MainScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}