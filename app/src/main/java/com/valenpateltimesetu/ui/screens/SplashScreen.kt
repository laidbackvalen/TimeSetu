package com.valenpateltimesetu.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavHostController
import android.app.Activity
import android.view.View
import android.view.WindowManager
import com.valenpateltimesetu.data.PreferencesManager
import com.valenpateltimesetu.ui.quotes.splashQuotes
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val angle: Float
)

@Composable
fun AnimatedBackgroundParticles() {
    val particles = remember {
        (0..30).map {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4f + 2f,
                speed = Random.nextFloat() * 0.5f + 0.2f,
                angle = Random.nextFloat() * 360f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        particles.forEach { particle ->
            val newX = (particle.x + cos(Math.toRadians(particle.angle.toDouble())).toFloat() * particle.speed * time) % 1f
            val newY = (particle.y + sin(Math.toRadians(particle.angle.toDouble())).toFloat() * particle.speed * time) % 1f
            
            val xPos = if (newX < 0) newX + 1f else newX
            val yPos = if (newY < 0) newY + 1f else newY
            
            drawCircle(
                color = themeColor.copy(alpha = 0.3f),
                radius = particle.size,
                center = Offset(xPos * width, yPos * height)
            )
        }
    }
}

@Composable
fun RotatingRing(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp,
    strokeWidth: Float
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ring")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    Canvas(modifier = modifier.size(size).rotate(rotation)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = minOf(this.size.width, this.size.height) / 2 - strokeWidth
        
        // Outer ring
        drawCircle(
            color = themeColor.copy(alpha = 0.3f),
            radius = radius,
            center = center,
            style = Stroke(width = strokeWidth)
        )
        
        // Animated arc
        drawArc(
            color = themeColor,
            startAngle = -90f,
            sweepAngle = 120f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight = FontWeight.Normal
) {
    var displayedText by remember { mutableStateOf("") }
    var currentIndex by remember { mutableStateOf(0) }
    
    LaunchedEffect(text) {
        while (currentIndex < text.length) {
            delay(50)
            displayedText += text[currentIndex]
            currentIndex++
        }
    }
    
    Text(
        text = displayedText,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun SplashScreen(navController: NavHostController, preferencesManager: PreferencesManager) {
    val view = LocalView.current
    
    // Hide all system bars for full screen immersive mode
    DisposableEffect(Unit) {
        val window = (view.context as? Activity)?.window
        window?.let {
            // Enable edge-to-edge
            WindowCompat.setDecorFitsSystemWindows(it, false)
            
            // Get the decor view
            val decorView = it.decorView
            
            // Post to ensure view is fully attached
            decorView.post {
                // Set window flags for fullscreen
                it.addFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
                
                // Use WindowInsetsControllerCompat to hide system bars
                val insetsController = WindowInsetsControllerCompat(it, view)
                insetsController.hide(WindowInsetsCompat.Type.statusBars())
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                insetsController.systemBarsBehavior = 
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                
                // Set system UI visibility flags directly for maximum compatibility
                // This is the key - using IMMERSIVE_STICKY to keep bars hidden
                decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
            }
        }
        
        onDispose {
            val window = (view.context as? Activity)?.window
            window?.let {
                val decorView = it.decorView
                decorView.post {
                    // Clear fullscreen flags
                    it.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    
                    val insetsController = WindowInsetsControllerCompat(it, view)
                    insetsController.show(WindowInsetsCompat.Type.systemBars())
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                }
            }
        }
    }
    
    val randomQuotes by remember { mutableStateOf(splashQuotes.random()) }
    var startAnimation by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    var showQuote by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }

    // Logo animations
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )
    
    val logoRotation by animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
        label = "logoRotation"
    )

    // Content animations
    val contentAlpha by animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "contentAlpha"
    )
    
    val contentOffsetY by animateDpAsState(
        targetValue = if (showContent) 0.dp else 50.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "contentOffsetY"
    )

    // Quote animation
    val quoteAlpha by animateFloatAsState(
        targetValue = if (showQuote) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "quoteAlpha"
    )
    
    val quoteScale by animateFloatAsState(
        targetValue = if (showQuote) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "quoteScale"
    )

    // Pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(300)
        showContent = true
        delay(800)
        showTagline = true
        delay(1200)
        showQuote = true
        delay(6000)
        
        // Navigate based on onboarding status
        if (preferencesManager.isOnboardingCompleted()) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("onboarding") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A0F2E),
                        Color(0xFF0A0A0F),
                        Color(0xFF1A1A2E),
                        backgroundColor
                    ),
                    center = Offset(0f, 0f)
                )
            )
    ) {
        // Animated background particles
        AnimatedBackgroundParticles()
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Logo section with dynamic animations
            Column(
                modifier = Modifier
                    .alpha(contentAlpha)
                    .offset(y = contentOffsetY),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Tagline with fade-in
                if (showTagline) {
                    Text(
                        text = "Minimal Distraction, Maximum Results",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 60.dp)
                            .alpha(if (showTagline) 1f else 0f)
                    )
                }

                // Rotating rings around logo
                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer rotating ring
                    RotatingRing(
                        modifier = Modifier.size(200.dp),
                        size = 200.dp,
                        strokeWidth = 3f
                    )
                    
                    // Middle ring (counter-rotate)
                    RotatingRing(
                        modifier = Modifier
                            .size(160.dp)
                            .rotate(-180f),
                        size = 160.dp,
                        strokeWidth = 2f
                    )
                    
                    // Central logo with glassmorphism
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .scale(logoScale * pulseScale)
                            .rotate(logoRotation)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        themeColor.copy(alpha = glowAlpha),
                                        themeColor.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = 40.dp,
                                shape = CircleShape,
                                spotColor = themeColor.copy(alpha = 0.6f)
                            )
                            .border(
                                width = 3.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.5f),
                                        themeColor.copy(alpha = 0.8f),
                                        Color.White.copy(alpha = 0.4f)
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⏳",
                            fontSize = 60.sp
                        )
                    }
                }
            }

            // Bottom section with quote
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(quoteAlpha)
                    .scale(quoteScale),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                // Quote text
                Text(
                    text = "\"$randomQuotes\"",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f),
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    letterSpacing = 0.3.sp,
                    modifier = Modifier.padding(bottom = 80.dp).fillMaxWidth(0.85f)
                )
            }

        }
    }
}
