package com.valenpateltimesetu.ui.screens.features

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor

@Composable
fun CognitiveGamesScreen(navController: NavController) {
    val games = listOf(
        Game("Memory Match", "Match pairs of cards", Icons.Default.Edit, "< 1 min"),
        Game("Quick Math Quiz", "Solve math problems fast", Icons.Default.Add, "< 1 min"),
        Game("Vocabulary Challenge", "Learn new words", Icons.Default.Edit, "< 1 min"),
        Game("Pattern Recognition", "Find the pattern", Icons.Default.Edit, "< 1 min")
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
                    text = "🧩 Cognitive Games",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Play mini-games during breaks to improve cognitive skills",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            items(games) { game ->
                GameCard(game)
            }
        }
    }
}

data class Game(
    val name: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val duration: String
)

@Composable
fun GameCard(game: Game) {
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
                    imageVector = game.icon,
                    contentDescription = game.name,
                    tint = themeColor,
                    modifier = Modifier.size(48.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = game.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = game.description,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Duration: ${game.duration}",
                        fontSize = 12.sp,
                        color = themeColor,
                        fontWeight = FontWeight.Medium
                    )
                }
                Button(
                    onClick = { /* Play game */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColor
                    )
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                }
            }
        }
    }
}

