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
fun FocusInsightsScreen(navController: NavController) {
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
                    text = "🧠 AI Focus Insights",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Data-driven insights about your focus patterns",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            item {
                InsightCard(
                    title = "Best Productivity Time",
                    value = "4 PM - 6 PM",
                    icon = Icons.Default.Add,
                    description = "You focus best during these hours"
                )
            }
            
            item {
                InsightCard(
                    title = "Longest Subject",
                    value = "Mathematics",
                    icon = Icons.Default.Edit,
                    description = "Takes an average of 28 minutes per session"
                )
            }
            
            item {
                InsightCard(
                    title = "Distraction Pattern",
                    value = "Thursdays",
                    icon = Icons.Default.Info,
                    description = "Distraction spikes detected on Thursdays"
                )
            }
            
            item {
                InsightCard(
                    title = "Peak Performance",
                    value = "After Lunch",
                    icon = Icons.Default.Star,
                    description = "Most productive sessions happen after lunch"
                )
            }
            
            item {
                InsightCard(
                    title = "Reading Speed",
                    value = "Mornings",
                    icon = Icons.Default.Add,
                    description = "You read faster in morning sessions"
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
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "📊 Hourly Focus Graph",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.1f),
                                        RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Chart visualization\n(Coming soon)",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InsightCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = themeColor,
                    modifier = Modifier.size(40.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

