package com.valenpateltimesetu.ui.screens.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor

@Composable
fun HeroCardsScreen(navController: NavController) {
    var currentLevel by remember { mutableStateOf(5) }
    var currentXP by remember { mutableStateOf(1250) }
    var totalXP by remember { mutableStateOf(2000) }
    
    val achievements = listOf(
        Achievement("Focus Wizard", "Completed 10 sessions", true, Icons.Default.Star),
        Achievement("Math Knight", "Solved 5 equations", true, Icons.Default.Edit),
        Achievement("No-Distraction Ninja", "Stayed focused for 1 hour", false, Icons.Default.Edit),
        Achievement("Reading Master", "Completed 20 reading sessions", false, Icons.Default.Edit),
        Achievement("Week Warrior", "7-day streak", true, Icons.Default.Edit)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "🏆 Hero Cards & Levels",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Earn cards, XP, and unlock achievements",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        themeColor.copy(alpha = 0.3f),
                                        themeColor.copy(alpha = 0.15f)
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Level $currentLevel",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "$currentXP / $totalXP XP",
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = themeColor,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            
                            LinearProgressIndicator(
                                progress = currentXP.toFloat() / totalXP.toFloat(),
                                modifier = Modifier.fillMaxWidth(),
                                color = themeColor,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Your Hero Cards",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(5) { index ->
                        HeroCard(
                            title = "Card ${index + 1}",
                            isUnlocked = index < 3
                        )
                    }
                }
            }
            
            item {
                Text(
                    text = "Achievements",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            items(achievements) { achievement ->
                AchievementCard(achievement)
            }
        }
    }
}

@Composable
fun HeroCard(title: String, isUnlocked: Boolean) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (isUnlocked) {
                        Brush.linearGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.4f),
                                themeColor.copy(alpha = 0.2f)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.White.copy(alpha = 0.05f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.Star else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) themeColor else Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isUnlocked) Color.White else Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

data class Achievement(
    val title: String,
    val description: String,
    val unlocked: Boolean,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = if (achievement.unlocked) {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.White.copy(alpha = 0.08f)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.08f),
                                Color.White.copy(alpha = 0.04f)
                            )
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = achievement.icon,
                    contentDescription = achievement.title,
                    tint = if (achievement.unlocked) themeColor else Color.White.copy(alpha = 0.3f),
                    modifier = Modifier.size(40.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = achievement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (achievement.unlocked) Color.White else Color.White.copy(alpha = 0.5f)
                    )
                    Text(
                        text = achievement.description,
                        fontSize = 12.sp,
                        color = if (achievement.unlocked) Color.White.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.4f)
                    )
                }
                if (achievement.unlocked) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Unlocked",
                        tint = Color.Green,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

