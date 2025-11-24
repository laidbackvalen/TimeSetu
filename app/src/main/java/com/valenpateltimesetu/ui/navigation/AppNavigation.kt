package com.valenpateltimesetu.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.valenpateltimesetu.data.PreferencesManager
import com.valenpateltimesetu.ui.navigation.bottomnav.BottomNavigationBar
import com.valenpateltimesetu.ui.screens.HomeScreen
import com.valenpateltimesetu.ui.screens.OnboardingScreen
import com.valenpateltimesetu.ui.screens.PrivacyPolicyScreen
import com.valenpateltimesetu.ui.screens.SettingsScreen
import com.valenpateltimesetu.ui.screens.SplashScreen
import com.valenpateltimesetu.ui.screens.TermsConditionsScreen
import com.valenpateltimesetu.ui.screens.UserGuideScreen
import com.valenpateltimesetu.ui.screens.features.*
import com.valenpateltimesetu.ui.theme.backgroundColor

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBottomBar = currentRoute != "splash" && currentRoute != "onboarding" && currentRoute != "privacy_policy" && currentRoute != "terms_conditions" && currentRoute != "user_guide"
    
    // Check onboarding status when splash screen loads
    LaunchedEffect(Unit) {
        // This will be handled in SplashScreen navigation
    }

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
                composable("splash") { 
                    SplashScreen(navController, preferencesManager) 
                }
                composable("onboarding") { 
                    OnboardingScreen(navController) 
                }
                composable("home") { HomeScreen(navController = navController) }
                composable("settings") { SettingsScreen(navController) }
                composable("privacy_policy") { PrivacyPolicyScreen(navController) }
                composable("terms_conditions") { TermsConditionsScreen(navController) }
                composable("user_guide") { UserGuideScreen(navController) }
                
                // Feature Screens
                composable("adaptive_focus") { AdaptiveFocusModeScreen(navController) }
                composable("study_snapshot") { StudySnapshotScreen(navController) }
                composable("life_equation") { LifeEquationScreen(navController) }
                composable("session_reflection") { SessionReflectionScreen(navController) }
                composable("focus_insights") { FocusInsightsScreen(navController) }
                composable("parent_dashboard") { ParentDashboardScreen(navController) }
                composable("hero_cards") { HeroCardsScreen(navController) }
                composable("routines") { RoutinesScreen(navController) }
                composable("distraction_lock") { DistractionLockScreen(navController) }
                composable("cognitive_games") { CognitiveGamesScreen(navController) }
                composable("emotional_wellness") { EmotionalWellnessScreen(navController) }
            }
        }
    }
}

