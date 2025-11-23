package com.valenpateltimesetu.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class IntroSlide(
    val title: String,
    val description: String,
    val icon: String,
    val backgroundGradient: List<Color>
)

val introSlides = listOf(
    IntroSlide(
        title = "Welcome to TimeSetu",
        description = "Master your time with the powerful Pomodoro technique. Focus, break, and achieve more.",
        icon = "⏳",
        backgroundGradient = listOf(
            Color(0xFF1A0F2E),
            Color(0xFF0A0A0F),
            Color(0xFF1A1A2E),
            backgroundColor
        )
    ),
    IntroSlide(
        title = "Focus & Achieve",
        description = "Work in focused 25-minute intervals. Take breaks to recharge. Build better habits every day.",
        icon = "🎯",
        backgroundGradient = listOf(
            Color(0xFF0F1A2E),
            Color(0xFF1A0F2E),
            Color(0xFF0A0A0F),
            backgroundColor
        )
    ),
    IntroSlide(
        title = "Ready to Start?",
        description = "Let's begin your journey to better time management. Swipe to get started!",
        icon = "🚀",
        backgroundGradient = listOf(
            Color(0xFF1A0F2E),
            Color(0xFF0F1A2E),
            Color(0xFF1A1A2E),
            backgroundColor
        )
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    
    // Calculate background gradient based on current page
    val currentPage = pagerState.currentPage
    val currentPageOffset = pagerState.currentPageOffset
    val pageIndex = currentPage.coerceIn(0, introSlides.size - 1)
    val nextPageIndex = (currentPage + 1).coerceIn(0, introSlides.size - 1)
    val scrollProgress = abs(currentPageOffset)
    
    val currentGradient = introSlides[pageIndex].backgroundGradient
    val nextGradient = introSlides[nextPageIndex].backgroundGradient
    
    // Interpolate between gradients
    val blendedGradient = remember(currentGradient, nextGradient, scrollProgress) {
        currentGradient.mapIndexed { index, color ->
            val nextColor = nextGradient.getOrElse(index) { color }
            Color(
                red = (color.red + (nextColor.red - color.red) * scrollProgress).coerceIn(0f, 1f),
                green = (color.green + (nextColor.green - color.green) * scrollProgress).coerceIn(0f, 1f),
                blue = (color.blue + (nextColor.blue - color.blue) * scrollProgress).coerceIn(0f, 1f),
                alpha = (color.alpha + (nextColor.alpha - color.alpha) * scrollProgress).coerceIn(0f, 1f)
            )
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(blendedGradient)
            )
    ) {
        // Animated background particles
        AnimatedBackgroundParticles(currentPage + abs(currentPageOffset))
        
        // Horizontal pager with zoom/fade transitions
        HorizontalPager(
            count = introSlides.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ZoomFadeSlide(
                slide = introSlides[page],
                pageIndex = page,
                pagerState = pagerState,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Page indicators
        PageIndicators(
            currentPage = currentPage + abs(currentPageOffset),
            totalPages = introSlides.size,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )
        
        // Get Started button
        if (currentPage >= introSlides.size - 1) {
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
                    .alpha(if (currentPage >= introSlides.size - 1) 1f else 0f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColor
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ZoomFadeSlide(
    slide: IntroSlide,
    pageIndex: Int,
    pagerState: com.google.accompanist.pager.PagerState,
    modifier: Modifier = Modifier
) {
    val pageOffset = (pagerState.currentPage - pageIndex) + pagerState.currentPageOffset
    val offset = abs(pageOffset)
    
    // Zoom effect: slides zoom in/out as they transition
    val scale = when {
        offset < 0.5f -> 0.8f + (1f - offset * 2f) * 0.2f // Zoom in from 0.8 to 1.0
        else -> 1f - (offset - 0.5f) * 0.4f // Zoom out from 1.0 to 0.8
    }
    
    // Fade effect
    val alpha = when {
        offset < 0.5f -> 0.3f + (1f - offset * 2f) * 0.7f // Fade in
        else -> 1f - (offset - 0.5f) * 2f // Fade out
    }
    
    // Vertical offset for slide up/down effect
    val verticalOffset = pageOffset * 100f
    
    // Blur effect for depth
    val blur = if (offset > 0.5f) (offset - 0.5f) * 20f else 0f
    
    Column(
        modifier = modifier
            .alpha(alpha.coerceIn(0f, 1f))
            .scale(scale.coerceIn(0.6f, 1.2f))
            .offset(y = verticalOffset.dp)
            .blur(blur.dp)
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with pulse animation
        Text(
            text = slide.icon,
            fontSize = 140.sp,
            modifier = Modifier
                .scale(1f + (1f - offset.coerceIn(0f, 1f)) * 0.15f)
        )
        
        Spacer(modifier = Modifier.height(50.dp))
        
        // Title
        Text(
            text = slide.title,
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            letterSpacing = 1.5.sp,
            modifier = Modifier.alpha(alpha.coerceIn(0f, 1f))
        )
        
        Spacer(modifier = Modifier.height(30.dp))
        
        // Description
        Text(
            text = slide.description,
            fontSize = 19.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.9f),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .alpha((alpha * 0.9f).coerceIn(0f, 1f))
        )
    }
}

@Composable
fun PageIndicators(
    currentPage: Float,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(totalPages) { index ->
            val pageOffset = abs(currentPage - index)
            val isActive = pageOffset < 0.5f
            val scale = if (isActive) 1.4f else 1f
            val alpha = (1f - pageOffset.coerceIn(0f, 1f))
            
            Box(
                modifier = Modifier
                    .size(if (isActive) 14.dp else 10.dp)
                    .scale(scale)
                    .alpha(alpha)
                    .background(
                        color = if (isActive) themeColor else Color.White.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun AnimatedBackgroundParticles(currentPage: Float) {
    val particles = remember {
        (0..25).map {
            Particle(
                x = kotlin.random.Random.nextFloat(),
                y = kotlin.random.Random.nextFloat(),
                size = kotlin.random.Random.nextFloat() * 3f + 1.5f,
                speed = kotlin.random.Random.nextFloat() * 0.3f + 0.1f,
                angle = kotlin.random.Random.nextFloat() * 360f
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

data class Particle(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val angle: Float
)
