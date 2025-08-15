package com.timesetu.ui.navigation.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItems(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItems("home", "Home", Icons.Default.Home)
    object Settings : BottomNavItems("settings", "Settings", Icons.Default.Settings)
}