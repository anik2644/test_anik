package com.example.test_anik.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.test_anik.presentation.screens.*

@Composable
fun ToLetNavGraph(
    navController: NavHostController,
    onBackToHome: () -> Unit
) {
    // This will hold our properties across navigation
    val propertiesList = remember { mutableStateListOf<ToLetProperty>().apply {
        addAll(getToLetProperties())
    } }

    NavHost(
        navController = navController,
        startDestination = ToLetScreen.ToLetList.route
    ) {
        composable(ToLetScreen.ToLetList.route) {
            ToLetListScreen(
                properties = propertiesList,
                onBackClick = onBackToHome,
                onPropertyClick = { property ->
                    navController.navigate(ToLetScreen.ToLetDetails.createRoute(property.id))
                },
                onAddPropertyClick = {
                    navController.navigate(ToLetScreen.AddToLet.route)
                }
            )
        }

        composable(ToLetScreen.ToLetDetails.route) { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")?.toIntOrNull()
            val property = propertiesList.find { it.id == propertyId }

            property?.let {
                ToLetDetailsScreen(
                    property = it,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onBookClick = {
                        // Handle booking
                    }
                )
            }
        }

        composable(ToLetScreen.AddToLet.route) {
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