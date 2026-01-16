package com.example.test_anik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test_anik.presentation.screens.MainScreen
import com.example.test_anik.presentation.screens.ModernLoginScreen1
import com.example.test_anik.presentation.screens.ModernRegistrationScreen1

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
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }

        composable(route = "register") {
            ModernRegistrationScreen1(
                onRegisterClick = { _, _, _, _ ->
                    // After successful registration, navigate to main
                    navController.navigate("main") {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    // Navigate back to login screen
                    navController.popBackStack()
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