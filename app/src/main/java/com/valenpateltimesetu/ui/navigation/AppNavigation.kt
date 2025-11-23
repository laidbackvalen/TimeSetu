package com.valenpateltimesetu.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.valenpateltimesetu.ui.navigation.bottomnav.BottomNavigationBar
import com.valenpateltimesetu.ui.screens.HomeScreen
import com.valenpateltimesetu.ui.screens.SettingsScreen
import com.valenpateltimesetu.ui.screens.SplashScreen
import com.valenpateltimesetu.ui.theme.backgroundColor
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute != "splash"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
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
}

