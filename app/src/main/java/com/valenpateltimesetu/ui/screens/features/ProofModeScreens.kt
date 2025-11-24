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
fun StudySnapshotScreen(navController: NavController) {
    var capturedPhotos by remember { mutableStateOf<List<String>>(emptyList()) }
    
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
                    text = "📸 Study Snapshot",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Capture your work to earn hero cards",
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
                            .height(200.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.15f),
                                        Color.White.copy(alpha = 0.08f)
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Camera",
                                tint = themeColor,
                                modifier = Modifier.size(64.dp)
                            )
                            Button(
                                onClick = { /* Open camera */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = themeColor
                                )
                            ) {
                                Text("Take Photo")
                            }
                            Text(
                                text = "Capture: Notebook, Math equation, Reading page, Schoolwork",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Recent Snapshots",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            if (capturedPhotos.isEmpty()) {
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
                                            Color.White.copy(alpha = 0.1f),
                                            Color.White.copy(alpha = 0.05f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No snapshots yet. Start capturing your work!",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LifeEquationScreen(navController: NavController) {
    var equation by remember { mutableStateOf("5 + 3 = ?") }
    var userAnswer by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    
    val correctAnswer = 8
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "🧮 Life Equation Mode",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Solve the puzzle to unlock Pomodoro",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    shape = RoundedCornerShape(24.dp),
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
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Text(
                                text = equation,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            OutlinedTextField(
                                value = userAnswer,
                                onValueChange = { userAnswer = it },
                                label = { Text("Your Answer") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = themeColor,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                                )
                            )
                            
                            Button(
                                onClick = {
                                    isCorrect = userAnswer.toIntOrNull() == correctAnswer
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = themeColor
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Check Answer", fontSize = 18.sp)
                            }
                            
                            if (isCorrect == true) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color.Green,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Correct! Pomodoro unlocked! 🎉",
                                        fontSize = 16.sp,
                                        color = Color.Green,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            } else if (isCorrect == false) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Try again!",
                                        fontSize = 16.sp,
                                        color = Color.Red
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

@Composable
fun SessionReflectionScreen(navController: NavController) {
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var learnedText by remember { mutableStateOf("") }
    var voiceNoteRecorded by remember { mutableStateOf(false) }
    
    val moods = listOf("😊 Happy", "😐 Okay", "😫 Tired", "😡 Stressed", "😌 Calm")
    
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
                    text = "📝 End-of-Session Reflection",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Reflect on your learning session",
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
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "How are you feeling?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                moods.forEach { mood ->
                                    FilterChip(
                                        selected = selectedMood == mood,
                                        onClick = { selectedMood = mood },
                                        label = { Text(mood, fontSize = 20.sp) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
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
                                text = "What did you learn today?",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            
                            OutlinedTextField(
                                value = learnedText,
                                onValueChange = { learnedText = it },
                                label = { Text("Share your learning") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = themeColor,
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                }
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
                                text = "Voice Note (Optional)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            
                            Button(
                                onClick = { voiceNoteRecorded = !voiceNoteRecorded },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (voiceNoteRecorded) Color.Green else themeColor
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = if (voiceNoteRecorded) Icons.Default.CheckCircle else Icons.Default.Edit,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(if (voiceNoteRecorded) "Voice Note Recorded" else "Record Voice Note")
                            }
                            
                            if (voiceNoteRecorded) {
                                Text(
                                    text = "Voice note will be sent to parent",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Button(
                    onClick = { /* Save reflection */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Reflection", fontSize = 18.sp)
                }
            }
        }
    }
}

