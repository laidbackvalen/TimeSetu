package com.valenpateltimesetu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.valenpateltimesetu.ui.navigation.AppNavigation
import com.valenpateltimesetu.ui.theme.TimeSetuTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            TimeSetuTheme {
                AppNavigation()
            }
        }
    }
}
