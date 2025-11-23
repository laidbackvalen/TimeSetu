package com.valenpateltimesetu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.valenpateltimesetu.data.PreferencesManager
import com.valenpateltimesetu.ui.theme.backgroundColor
import androidx.compose.material3.Text
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController, preferencesManager: PreferencesManager) {
    // Navigate based on onboarding status
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        if (preferencesManager.isOnboardingCompleted()) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0A0F),
                        Color(0xFF1A1A2E),
                        backgroundColor
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TimeSetu",
            fontSize = 48.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = 2.sp
        )
    }
}
