package com.example.test_anik.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Rounded.Home
    )

    object Chat : BottomBarScreen(
        route = "chat",
        title = "Chat",
        icon = Icons.Rounded.Chat
    )

    object Notification : BottomBarScreen(
        route = "notification",
        title = "Notification",
        icon = Icons.Rounded.Notifications
    )

    object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = Icons.Rounded.Person
    )
}