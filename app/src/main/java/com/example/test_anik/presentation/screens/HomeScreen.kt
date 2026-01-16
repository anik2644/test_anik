package com.example.test_anik.presentation.screens

import android.R.attr.name
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Localized description")
                }
            )
        }
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            Text(
                text = "Hello Mahdi!",
                modifier = modifier
            )
            Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Localized description")
        }
    }
}