package com.valenpateltimesetu.ui.screens.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun EmotionalWellnessScreen(navController: NavController) {
    var selectedMood by remember { mutableStateOf<String?>(null) }
    
    val moods = listOf(
        Mood("😊", "Happy", "Great! Let's keep the energy up!"),
        Mood("😐", "Okay", "We'll adjust the session to help you feel better"),
        Mood("😫", "Tired", "Shorter focus blocks and longer breaks"),
        Mood("😡", "Stressed", "Gentle encouragement and breathing exercises")
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
                    text = "❤️ Emotional Check-In",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "How are you feeling today?",
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
                            Text(
                                text = "Select your mood",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                moods.forEach { mood ->
                                    MoodButton(
                                        mood = mood,
                                        isSelected = selectedMood == mood.emoji,
                                        onClick = { selectedMood = mood.emoji }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            if (selectedMood != null) {
                val selectedMoodData = moods.find { it.emoji == selectedMood }
                if (selectedMoodData != null) {
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
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.White.copy(alpha = 0.15f),
                                                Color.White.copy(alpha = 0.08f)
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(20.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(
                                        text = "App Adjustments",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = selectedMoodData.adjustment,
                                        fontSize = 14.sp,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    
                                    Divider(color = Color.White.copy(alpha = 0.2f))
                                    
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        AdjustmentItem(
                                            icon = Icons.Default.Edit,
                                            label = "Pomodoro Length",
                                            value = when (selectedMood) {
                                                "😊" -> "25 min"
                                                "😐" -> "20 min"
                                                "😫" -> "15 min"
                                                "😡" -> "15 min"
                                                else -> "20 min"
                                            }
                                        )
                                        AdjustmentItem(
                                            icon = Icons.Default.Edit,
                                            label = "Break Time",
                                            value = when (selectedMood) {
                                                "😊" -> "5 min"
                                                "😐" -> "5 min"
                                                "😫" -> "10 min"
                                                "😡" -> "10 min"
                                                else -> "5 min"
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Mood(
    val emoji: String,
    val label: String,
    val adjustment: String
)

@Composable
fun RowScope.MoodButton(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) themeColor else Color.Transparent
        ),
        modifier = Modifier.weight(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = mood.emoji,
                fontSize = 32.sp
            )
            Text(
                text = mood.label,
                fontSize = 12.sp,
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun RowScope.AdjustmentItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color.White.copy(alpha = 0.1f),
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = themeColor,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

