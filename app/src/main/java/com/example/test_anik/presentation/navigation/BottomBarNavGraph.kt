package com.example.test_anik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.test_anik.presentation.screens.*

@Composable
fun BottomBarNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit = {}
) {
    // Shared properties list that persists across navigation
    val propertiesList = remember {
        mutableStateListOf<ToLetProperty>().apply {
            addAll(getToLetProperties())
        }
    }

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Chat.route) {
            ChatScreen()
        }
        composable(route = BottomBarScreen.Notification.route) {
            NotificationScreen()
        }
        composable(route = BottomBarScreen.Profile.route) {
            ProfileScreen(
                onLogoutClick = onLogout
            )
        }

        // To-Let List Screen
        composable(route = ToLetScreen.ToLetList.route) {
            ToLetListScreen(
                properties = propertiesList,
                onBackClick = {
                    navController.popBackStack()
                },
                onPropertyClick = { property ->
                    navController.navigate(ToLetScreen.ToLetDetails.createRoute(property.id))
                },
                onAddPropertyClick = {
                    navController.navigate(ToLetScreen.AddToLet.route)
                }
            )
        }

        // To-Let Details Screen
        composable(
            route = ToLetScreen.ToLetDetails.route,
            arguments = listOf(navArgument("propertyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getInt("propertyId")
            val property = propertiesList.find { it.id == propertyId }

            property?.let {
                ToLetDetailsScreen(
                    property = it,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onBookClick = {
                        // Handle booking action
                    }
                )
            }
        }

        // Add To-Let Screen
        composable(route = ToLetScreen.AddToLet.route) {
            AddToLetScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onPostCreated = { newProperty ->
                    // Add the new property to the list
                    propertiesList.add(0, newProperty)
                    // Navigate back to list
                    navController.popBackStack()
                }
            )
        }
    }
}