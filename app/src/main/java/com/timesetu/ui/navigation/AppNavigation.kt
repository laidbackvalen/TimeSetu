package com.timesetu.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.timesetu.ui.navigation.bottomnav.BottomNavigtionBar
import com.timesetu.ui.screens.HomeScreen
import com.timesetu.ui.screens.SettingsScreen
import com.timesetu.ui.screens.SplashScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute != "splash"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigtionBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "splash",
            modifier = if (showBottomBar) {
                Modifier.padding(innerPadding)
            } else {
                Modifier
            }
        ) {
            composable("splash") { SplashScreen(navController) }
            composable("home") { HomeScreen() }
            composable("settings") { SettingsScreen() }
        }
    }
}

