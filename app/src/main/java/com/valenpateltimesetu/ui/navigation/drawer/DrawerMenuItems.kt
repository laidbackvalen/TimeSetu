package com.valenpateltimesetu.ui.navigation.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class DrawerMenuItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val section: DrawerSection
) {
    // 🚀 Adaptive Focus Mode Section
    object AdaptiveFocusMode : DrawerMenuItem(
        route = "adaptive_focus",
        title = "Adaptive Focus Mode",
        icon = Icons.Default.Star,
        section = DrawerSection.ADAPTIVE_FOCUS
    )
    
    // 📸 Proof Mode Section
    object StudySnapshot : DrawerMenuItem(
        route = "study_snapshot",
        title = "Study Snapshot",
        icon = Icons.Default.Edit,
        section = DrawerSection.PROOF_MODE
    )
    
    object LifeEquation : DrawerMenuItem(
        route = "life_equation",
        title = "Life Equation Mode",
        icon = Icons.Default.Lock,
        section = DrawerSection.PROOF_MODE
    )
    
    object SessionReflection : DrawerMenuItem(
        route = "session_reflection",
        title = "End-of-Session Reflection",
        icon = Icons.Default.Edit,
        section = DrawerSection.PROOF_MODE
    )
    
    // 🧠 AI Insights Section
    object FocusInsights : DrawerMenuItem(
        route = "focus_insights",
        title = "AI Focus Insights",
        icon = Icons.Default.Favorite,
        section = DrawerSection.AI_INSIGHTS
    )
    
    // 👨‍👩‍👧 Parent Dashboard Section
    object ParentDashboard : DrawerMenuItem(
        route = "parent_dashboard",
        title = "Parent Dashboard",
        icon = Icons.Default.AccountBox,
        section = DrawerSection.PARENT_DASHBOARD
    )
    
    // 🏆 Hero Cards Section
    object HeroCards : DrawerMenuItem(
        route = "hero_cards",
        title = "Hero Cards & Levels",
        icon = Icons.Default.Star,
        section = DrawerSection.GAMIFICATION
    )
    
    // 🌈 Routines Section
    object Routines : DrawerMenuItem(
        route = "routines",
        title = "Study Routines",
        icon = Icons.Default.Star,
        section = DrawerSection.ROUTINES
    )
    
    // 🔕 Distraction Lock Section
    object DistractionLock : DrawerMenuItem(
        route = "distraction_lock",
        title = "Distraction Lock",
        icon = Icons.Default.Lock,
        section = DrawerSection.DISTRACTION_LOCK
    )
    
    // 🧩 Cognitive Games Section
    object CognitiveGames : DrawerMenuItem(
        route = "cognitive_games",
        title = "Cognitive Games",
        icon = Icons.Default.Add,
        section = DrawerSection.COGNITIVE_GAMES
    )
    
    // ❤️ Emotional Wellness Section
    object EmotionalWellness : DrawerMenuItem(
        route = "emotional_wellness",
        title = "Emotional Check-In",
        icon = Icons.Default.Favorite,
        section = DrawerSection.EMOTIONAL_WELLNESS
    )
}

enum class DrawerSection(val title: String) {
    ADAPTIVE_FOCUS("🚀 Adaptive Focus"),
    PROOF_MODE("📸 Proof Mode"),
    AI_INSIGHTS("🧠 AI Insights"),
    PARENT_DASHBOARD("👨‍👩‍👧 Parent Dashboard"),
    GAMIFICATION("🏆 Hero Cards & Levels"),
    ROUTINES("🌈 Routines"),
    DISTRACTION_LOCK("🔕 Distraction Lock"),
    COGNITIVE_GAMES("🧩 Cognitive Games"),
    EMOTIONAL_WELLNESS("❤️ Emotional Wellness")
}

