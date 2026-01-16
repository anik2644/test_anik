package com.example.test_anik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test_anik.presentation.screens.ChatScreen
import com.example.test_anik.presentation.screens.HomeScreen
import com.example.test_anik.presentation.screens.NotificationScreen
import com.example.test_anik.presentation.screens.ProfileScreen

@Composable
fun BottomBarNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit = {}  // Add this parameter
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen()
        }
        composable(route = BottomBarScreen.Chat.route) {
            ChatScreen()
        }
        composable(route = BottomBarScreen.Notification.route) {
            NotificationScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                onLogoutClick = onLogout  // Pass the logout callback
            )
        }
    }
}