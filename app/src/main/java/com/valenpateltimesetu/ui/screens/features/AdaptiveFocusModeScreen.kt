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
fun AdaptiveFocusModeScreen(navController: NavController) {
    var childAge by remember { mutableStateOf(10) }
    var pastPerformance by remember { mutableStateOf("Good") }
    var moodCheckIn by remember { mutableStateOf("Happy") }
    var subjectDifficulty by remember { mutableStateOf("Medium") }
    
    val recommendedFocusTime = when {
        childAge <= 6 -> 10
        childAge <= 9 -> 15
        childAge <= 12 -> 20
        else -> 25
    }
    
    val recommendedBreakTime = when {
        childAge <= 6 -> 2
        childAge <= 9 -> 3
        childAge <= 12 -> 5
        else -> 5
    }
    
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
                    text = "🚀 Adaptive Focus Mode",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "AI-powered Pomodoro that adapts to your child's needs",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
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
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Child's Age",
                                    tint = themeColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "Child's Age",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "$childAge years",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { if (childAge > 1) childAge-- },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = themeColor
                                    )
                                ) {
                                    Text("-")
                                }
                                Text(
                                    text = "$childAge",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 16.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Button(
                                    onClick = { if (childAge < 18) childAge++ },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = themeColor
                                    )
                                ) {
                                    Text("+")
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                FeatureCard(
                    title = "Past Performance",
                    value = pastPerformance,
                    icon = Icons.Default.Edit,
                    options = listOf("Excellent", "Good", "Average", "Needs Improvement")
                ) { pastPerformance = it }
            }
            
            item {
                FeatureCard(
                    title = "Mood Check-In",
                    value = moodCheckIn,
                    icon = Icons.Default.Edit,
                    options = listOf("Happy", "Okay", "Tired", "Stressed")
                ) { moodCheckIn = it }
            }
            
            item {
                FeatureCard(
                    title = "Subject Difficulty",
                    value = subjectDifficulty,
                    icon = Icons.Default.Edit,
                    options = listOf("Easy", "Medium", "Hard", "Very Hard")
                ) { subjectDifficulty = it }
            }
            
            item {
                RecommendationCard(
                    focusTime = recommendedFocusTime,
                    breakTime = recommendedBreakTime,
                    insights = listOf(
                        "Best focus time: 4 PM - 6 PM",
                        "Math sessions work best in shorter blocks",
                        "Reading comprehension improves in morning sessions"
                    )
                )
            }
        }
    }
}

@Composable
fun FeatureCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    options: List<String>? = null,
    onValueChange: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = themeColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                if (options != null) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        options.forEach { option ->
                            FilterChip(
                                selected = value == option,
                                onClick = { onValueChange(option) },
                                label = { Text(option, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(
    focusTime: Int,
    breakTime: Int,
    insights: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
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
                    text = "✨ AI Recommendations",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RecommendationItem(
                        label = "Focus Time",
                        value = "$focusTime min",
                        icon = Icons.Default.Edit
                    )
                    RecommendationItem(
                        label = "Break Time",
                        value = "$breakTime min",
                        icon = Icons.Default.Refresh
                    )
                }
                
                Divider(color = Color.White.copy(alpha = 0.2f))
                
                Text(
                    text = "Insights:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                
                insights.forEach { insight ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = themeColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = insight,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.RecommendationItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = themeColor,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = value,
            fontSize = 20.sp,
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

