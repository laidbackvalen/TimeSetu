package com.valenpateltimesetu.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.valenpateltimesetu.R
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGuideScreen(navController: NavController) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(themeColor, themeColor.copy(alpha = 0.7f))
                                    )
                                )
                                .border(2.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📚", fontSize = 20.sp)
                        }
                        Text(
                            text = "User Guide",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
        ) {
            // Hero Welcome Section
            item {
                WelcomeHeroCard()
            }

            // Getting Started
            item {
                ModernSectionCard(
                    title = "Getting Started",
                    icon = Icons.Default.PlayArrow,
                    gradientColors = listOf(themeColor, themeColor.copy(alpha = 0.6f)),
                    content = {
                        StepItem(number = "01", title = "Start the Timer", description = "Tap the Play button (▶) to begin your Pomodoro session. The timer will count down from your selected time.")
                        Spacer(modifier = Modifier.height(16.dp))
                        StepItem(number = "02", title = "Select Time Mode", description = "Choose from preset options (5 min, 15 min, or 25 min) or create your own custom time duration.")
                        Spacer(modifier = Modifier.height(16.dp))
                        StepItem(number = "03", title = "Set Custom Time", description = "Tap the edit icon (✏️) next to the timer, use number pickers to set minutes and seconds, then tap 'Set'.")
                    }
                )
            }

            // How to Use Timer
            item {
                ModernSectionCard(
                    title = "Timer Controls",
                    icon = Icons.Default.Settings,
                    gradientColors = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6)),
                    content = {
                        FeatureItem(
                            icon = Icons.Default.PlayArrow,
                            title = "Play",
                            description = "Starts the timer countdown"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureItem(
                            iconRes = R.drawable.ic_pause,
                            title = "Pause",
                            description = "Pauses the timer at current time"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureItem(
                            icon = Icons.Default.Refresh,
                            title = "Reset",
                            description = "Resets timer to original time"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FeatureItem(
                            icon = Icons.Default.Edit,
                            title = "Custom Time",
                            description = "Set any duration using the edit icon"
                        )
                    }
                )
            }

            // Pomodoro Technique
            item {
                ModernSectionCard(
                    title = "Pomodoro Technique",
                    icon = Icons.Default.Settings,
                    gradientColors = listOf(Color(0xFF10B981), Color(0xFF059669)),
                    content = {
                        Text(
                            text = "The proven time management method:",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.95f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        PomodoroStep(number = 1, text = "Work for 25 minutes (one Pomodoro)")
                        PomodoroStep(number = 2, text = "Take a 5-minute break")
                        PomodoroStep(number = 3, text = "Repeat 3-4 times")
                        PomodoroStep(number = 4, text = "Take a longer 15-30 minute break")
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Benefits",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        BenefitItem("✨ Improves focus and concentration")
                        BenefitItem("💪 Reduces mental fatigue")
                        BenefitItem("🚀 Enhances productivity")
                        BenefitItem("⏰ Better time management")
                    }
                )
            }

            // FAQs
            item {
                ModernSectionCard(
                    title = "Frequently Asked Questions",
                    icon = Icons.Default.Email,
                    gradientColors = listOf(Color(0xFFF59E0B), Color(0xFFD97706)),
                    content = {
                        ModernFAQItem(
                            question = "How do I set a custom time?",
                            answer = "Tap the edit icon (✏️) next to the timer display. Use the number pickers to set your desired minutes and seconds, then tap 'Set' to confirm."
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernFAQItem(
                            question = "Can I pause the timer?",
                            answer = "Yes! Tap the Play button while the timer is running to pause it. Tap again to resume from where you left off."
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernFAQItem(
                            question = "What happens when timer reaches zero?",
                            answer = "The timer stops and automatically resets to your original time. Use this as a signal to take a break or start your next work session."
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernFAQItem(
                            question = "Can I use custom times for Pomodoro?",
                            answer = "Absolutely! While the classic Pomodoro uses 25 minutes, you can customize it to any duration that works best for your workflow."
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernFAQItem(
                            question = "Does the timer work in background?",
                            answer = "Yes, the timer continues running when you switch apps or lock your device, allowing you to focus on your work while tracking time."
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ModernFAQItem(
                            question = "How do I reset the timer?",
                            answer = "Tap the Reset button (↻) at any time to return to your original set time, whether the timer is running, paused, or stopped."
                        )
                    }
                )
            }

            // Troubleshooting
            item {
                ModernSectionCard(
                    title = "Troubleshooting",
                    icon = Icons.Default.Settings,
                    gradientColors = listOf(Color(0xFFEF4444), Color(0xFFDC2626)),
                    content = {
                        IssueItem(
                            issue = "Timer not starting",
                            solutions = listOf(
                                "Make sure you have selected a time (default is 25 minutes)",
                                "Try resetting the timer and starting again"
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        IssueItem(
                            issue = "Custom time not saving",
                            solutions = listOf(
                                "Ensure you tap 'Set' after selecting your time",
                                "Check that minutes and seconds are within valid range (0-59)"
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        IssueItem(
                            issue = "Timer stops unexpectedly",
                            solutions = listOf(
                                "Check your device's battery optimization settings",
                                "Ensure the app has necessary permissions"
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        IssueItem(
                            issue = "App not responding",
                            solutions = listOf(
                                "Try closing and reopening the app",
                                "Restart your device if the issue persists"
                            )
                        )
                    }
                )
            }

            // Tips & Best Practices
            item {
                ModernSectionCard(
                    title = "Tips & Best Practices",
                    icon = Icons.Default.Star,
                    gradientColors = listOf(Color(0xFF8B5CF6), Color(0xFF7C3AED)),
                    content = {
                        TipItem("Start with the classic 25-minute Pomodoro")
                        TipItem("Take breaks seriously - they're essential for productivity")
                        TipItem("Adjust timer duration based on your tasks")
                        TipItem("Use shorter sessions (5-15 min) for quick tasks")
                        TipItem("Use longer sessions for deep work")
                        TipItem("Eliminate distractions during timer sessions")
                        TipItem("Track your completed Pomodoros")
                        TipItem("Be consistent with the technique")
                    }
                )
            }

            // Still Need Help Section
            item {
                PremiumSupportCard(context = context)
            }

            // Footer spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun WelcomeHeroCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            themeColor.copy(alpha = 0.15f),
                            Color(0xFF1E1E1E),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Welcome to TimeSetu",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Master your time with the Pomodoro Technique. Get started with our comprehensive guide.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(themeColor.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )
                        .border(2.dp, themeColor.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⏳", fontSize = 40.sp)
                }
            }
        }
    }
}

@Composable
fun ModernSectionCard(
    title: String,
    icon: ImageVector,
    gradientColors: List<Color>,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(gradientColors)
                        )
                        .border(1.dp, gradientColors[0].copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            
            Divider(
                color = Color.White.copy(alpha = 0.1f),
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            content()
        }
    }
}

@Composable
fun StepItem(number: String, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(themeColor.copy(alpha = 0.2f))
                .border(2.dp, themeColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = themeColor
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector? = null, iconRes: Int? = null, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            when {
                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = themeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                iconRes != null -> {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = themeColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun PomodoroStep(number: Int, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$number.",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF10B981)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 20.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BenefitItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 18.sp
        )
    }
}

@Composable
fun ModernFAQItem(question: String, answer: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Q:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF59E0B)
            )
            Text(
                text = question,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            text = "A: $answer",
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.85f),
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}

@Composable
fun IssueItem(issue: String, solutions: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFF4444).copy(alpha = 0.1f))
            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = Color(0xFFEF4444),
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = issue,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFEF4444)
            )
        }
        solutions.forEach { solution ->
            Row(
                modifier = Modifier.padding(start = 26.dp, top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "•",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = solution,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    lineHeight = 18.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TipItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFF8B5CF6),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.9f),
            lineHeight = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PremiumSupportCard(context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F172A)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            themeColor.copy(alpha = 0.2f),
                            Color(0xFF1E1E1E),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(themeColor, themeColor.copy(alpha = 0.7f))
                            )
                        )
                        .border(3.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                Text(
                    text = "Still Need Help?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "If you couldn't find the answer you're looking for, feel free to contact our support team. We're here to help!",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Button(
                    onClick = { sendSupportEmail(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColor
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Contact Support",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

fun sendSupportEmail(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:we.nullscape@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, "TimeSetu - App Support Request")
        putExtra(Intent.EXTRA_TEXT, "Hello Nullscape Team,\n\nI need help with:\n\n")
    }
    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    } catch (e: Exception) {
        // Handle error if no email app is available
    }
}
