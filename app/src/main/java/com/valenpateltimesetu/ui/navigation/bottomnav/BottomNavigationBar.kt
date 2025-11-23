package com.valenpateltimesetu.ui.navigation.bottomnav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.valenpateltimesetu.ui.theme.themeColor

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(BottomNavItems.Home, BottomNavItems.Settings)
    var rotationAngle by remember { mutableStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = tween(durationMillis = 300),
        label = "rotation"
    )

    NavigationBar(
        containerColor = Color.Black
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { item ->
            val selected = currentRoute == item.route
            val isSettings = item == BottomNavItems.Settings
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (isSettings) {
                        rotationAngle += 360f
                    }
                    navController.navigate(item.route) {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = Color.White,
                        modifier = if (isSettings) {
                            Modifier.rotate(animatedRotation)
                        } else {
                            Modifier
                        }
                    )
                },
                label = if (selected) {
                    { Text(text = item.label, color = Color.White) }
                } else null,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White,
                    selectedTextColor = themeColor,
                    indicatorColor = themeColor
                )
            )
        }
    }
}
