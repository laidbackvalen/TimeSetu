package com.valenpateltimesetu.ui.navigation.drawer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    navController: NavController,
    currentRoute: String?,
    onClose: () -> Unit
) {
    val menuItems = listOf(
        DrawerMenuItem.AdaptiveFocusMode,
        DrawerMenuItem.StudySnapshot,
        DrawerMenuItem.LifeEquation,
        DrawerMenuItem.SessionReflection,
        DrawerMenuItem.FocusInsights,
        DrawerMenuItem.ParentDashboard,
        DrawerMenuItem.HeroCards,
        DrawerMenuItem.Routines,
        DrawerMenuItem.DistractionLock,
        DrawerMenuItem.CognitiveGames,
        DrawerMenuItem.EmotionalWellness
    )
    
    val groupedItems = menuItems.groupBy { it.section }
    
    ModalDrawerSheet(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight(),
        drawerContainerColor = backgroundColor,
        drawerContentColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0A0F),
                            Color(0xFF1A1A2E),
                            backgroundColor
                        )
                    )
                )
        ) {
            // Header
            DrawerHeader()
            
            Divider(
                color = Color.White.copy(alpha = 0.1f),
                thickness = 1.dp
            )
            
            // Menu Items by Section
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                groupedItems.forEach { (section, items) ->
                    // Section Header
                    item {
                        SectionHeader(section.title)
                    }
                    
                    // Section Items
                    items(items) { item ->
                        DrawerMenuItem(
                            item = item,
                            isSelected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                }
                                onClose()
                            }
                        )
                    }
                    
                    // Spacer between sections
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        themeColor.copy(alpha = 0.3f),
                        themeColor.copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "TimeSetu",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Focus & Learn",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Premium Features",
                    fontSize = 14.sp,
                    color = themeColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White.copy(alpha = 0.6f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
fun DrawerMenuItem(
    item: DrawerMenuItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    Brush.linearGradient(
                        colors = listOf(
                            themeColor.copy(alpha = 0.3f),
                            themeColor.copy(alpha = 0.15f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = if (isSelected) themeColor else Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f),
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

