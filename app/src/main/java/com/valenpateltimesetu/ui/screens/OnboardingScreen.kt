package com.valenpateltimesetu.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.valenpateltimesetu.data.PreferencesManager
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class OnboardingSlide(
    val title: String,
    val subtitle: String,
    val description: String,
    val icon: String,
    val backgroundGradient: List<Color>
)

val onboardingSlides = listOf(
    OnboardingSlide(
        title = "Welcome to TimeSetu",
        subtitle = "Productivity Redefined",
        description = "Master your time with precision. Focus deeply, work efficiently, achieve consistently.",
        icon = "⏳",
        backgroundGradient = listOf(
            Color(0xFF0A0A0F),
            Color(0xFF1A1A2E),
            backgroundColor
        )
    ),
    OnboardingSlide(
        title = "Focus Sessions",
        subtitle = "Deep Work Mode",
        description = "Structured intervals designed for maximum concentration and sustained productivity.",
        icon = "🎯",
        backgroundGradient = listOf(
            Color(0xFF0F1A2E),
            Color(0xFF1A1A2E),
            backgroundColor
        )
    ),
    OnboardingSlide(
        title = "Track Progress",
        subtitle = "Build Better Habits",
        description = "Monitor your productivity and build better habits. Every session counts towards your goals.",
        icon = "📊",
        backgroundGradient = listOf(
            Color(0xFF1A0F2E),
            Color(0xFF0F1A2E),
            backgroundColor
        )
    ),
    OnboardingSlide(
        title = "Ready to Start?",
        subtitle = "Begin Your Journey",
        description = "Transform how you manage time. Start your first session and experience the difference.",
        icon = "🚀",
        backgroundGradient = listOf(
            Color(0xFF1A0F2E),
            Color(0xFF0F1A2E),
            backgroundColor
        )
    )
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    
    // Calculate background gradient based on current page
    val currentPage = pagerState.currentPage
    val currentPageOffset = pagerState.currentPageOffset
    val pageIndex = currentPage.coerceIn(0, onboardingSlides.size - 1)
    val nextPageIndex = (currentPage + 1).coerceIn(0, onboardingSlides.size - 1)
    val scrollProgress = abs(currentPageOffset)
    
    val currentGradient = onboardingSlides[pageIndex].backgroundGradient
    val nextGradient = onboardingSlides[nextPageIndex].backgroundGradient
    
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
        
        // Animated gradient orbs
        AnimatedGradientOrbs(currentPage + abs(currentPageOffset))
        
        // Skip button in top right corner
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 50.dp, end = 24.dp)
                .zIndex(10f)
        ) {
            GlassSkipButton(
                onClick = {
                    preferencesManager.setOnboardingCompleted(true)
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        
        // Horizontal pager
        HorizontalPager(
            count = onboardingSlides.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) { page ->
            OnboardingSlideContent(
                slide = onboardingSlides[page],
                pageIndex = page,
                pagerState = pagerState,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // Indicators below text (centered in middle area)
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 220.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                repeat(onboardingSlides.size) { index ->
                    val pageOffset = abs((currentPage + currentPageOffset) - index)
                    val isActive = pageOffset < 0.5f

                    // Animated indicator
                    AnimatedIndicator(
                        isActive = isActive,
                        pageOffset = pageOffset
                    )
                }
            }
        }
        
        // Next/Get Started button at bottom - placed after pager to ensure it's on top
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .zIndex(100f) // Higher zIndex to ensure it's above pager
        ) {
            if (currentPage < onboardingSlides.size - 1) {
                // Next button
                GlassButton(
                    text = "Next",
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(currentPage + 1)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Get Started button
                GlassButton(
                    text = "Get Started",
                    onClick = {
                        preferencesManager.setOnboardingCompleted(true)
                        navController.navigate("home") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    showIcon = true,
                    useThemeColor = true
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingSlideContent(
    slide: OnboardingSlide,
    pageIndex: Int,
    pagerState: com.google.accompanist.pager.PagerState,
    modifier: Modifier = Modifier
) {
    val pageOffset = (pagerState.currentPage - pageIndex) + pagerState.currentPageOffset
    val offset = abs(pageOffset)
    
    // Subtle fade and slide
    val alpha = when {
        offset < 0.5f -> 1f - offset * 2f
        else -> 0f
    }.coerceIn(0f, 1f)
    
    val horizontalOffset = pageOffset * 50f
    
    // Staggered animations for text elements
    val titleAlpha by animateFloatAsState(
        targetValue = if (alpha > 0.5f) alpha else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "titleAlpha"
    )
    
    val subtitleAlpha by animateFloatAsState(
        targetValue = if (alpha > 0.7f) alpha else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 100, easing = FastOutSlowInEasing),
        label = "subtitleAlpha"
    )
    
    val descriptionAlpha by animateFloatAsState(
        targetValue = if (alpha > 0.8f) alpha else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 200, easing = FastOutSlowInEasing),
        label = "descriptionAlpha"
    )
    
    val iconScale by animateFloatAsState(
        targetValue = if (alpha > 0.5f) 1f else 0.8f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "iconScale"
    )
    
    Column(
        modifier = modifier
            .alpha(alpha)
            .offset(x = horizontalOffset.dp)
            .padding(horizontal = 48.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated icon with pulse
        Box(
            modifier = Modifier
                .scale(iconScale)
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect behind icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.2f),
                                themeColor.copy(alpha = 0.0f)
                            )
                        ),
                        shape = CircleShape
                    )
            )
            Text(
                text = slide.icon,
                fontSize = 60.sp,
                modifier = Modifier.scale(iconScale)
            )
        }
        
        // Title with fade animation
        Text(
            text = slide.title,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .alpha(titleAlpha)
        )
        
        // Subtitle with fade animation
        Text(
            text = slide.subtitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
            modifier = Modifier
                .padding(bottom = 15.dp)
                .alpha(subtitleAlpha)
        )
        
        // Description with fade animation
        Text(
            text = slide.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.8f),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            letterSpacing = 0.3.sp,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(bottom = 150.dp)
                .alpha(descriptionAlpha)
        )
    }
}

@Composable
fun AnimatedIndicator(
    isActive: Boolean,
    pageOffset: Float
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.3f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "indicatorScale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isActive) 1f else 0.4f,
        animationSpec = tween(durationMillis = 300),
        label = "indicatorAlpha"
    )
    
    Box(
        modifier = Modifier
            .size(if (isActive) 10.dp else 8.dp)
            .scale(scale)
            .alpha(alpha)
            .background(
                color = if (isActive) themeColor else Color.White.copy(alpha = 0.5f),
                shape = CircleShape
            )
    )
}

@Composable
fun AnimatedBackgroundParticles(currentPage: Float) {
    val particles = remember {
        (0..30).map {
            Particle(
                x = kotlin.random.Random.nextFloat(),
                y = kotlin.random.Random.nextFloat(),
                size = kotlin.random.Random.nextFloat() * 4f + 2f,
                speed = kotlin.random.Random.nextFloat() * 0.4f + 0.1f,
                angle = kotlin.random.Random.nextFloat() * 360f
            )
        }
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
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
                color = themeColor.copy(alpha = 0.15f),
                radius = particle.size,
                center = Offset(xPos * width, yPos * height)
            )
        }
    }
}

@Composable
fun AnimatedGradientOrbs(currentPage: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbs")
    
    val orb1X by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1X"
    )
    
    val orb1Y by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb1Y"
    )
    
    val orb2X by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2X"
    )
    
    val orb2Y by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb2Y"
    )
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        // First orb
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    themeColor.copy(alpha = 0.1f),
                    themeColor.copy(alpha = 0.0f)
                ),
                radius = 200f
            ),
            radius = 200f,
            center = Offset(orb1X * width * 0.7f, orb1Y * height * 0.6f)
        )
        
        // Second orb
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.05f),
                    Color.White.copy(alpha = 0.0f)
                ),
                radius = 150f
            ),
            radius = 150f,
            center = Offset(orb2X * width * 0.8f + width * 0.2f, orb2Y * height * 0.7f)
        )
    }
}

@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
    useThemeColor: Boolean = false
) {
    val backgroundGradient = if (useThemeColor) {
        Brush.linearGradient(
            colors = listOf(
                themeColor.copy(alpha = 0.4f),
                themeColor.copy(alpha = 0.25f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.25f),
                Color.White.copy(alpha = 0.15f)
            )
        )
    }
    
    val borderGradient = if (useThemeColor) {
        Brush.linearGradient(
            colors = listOf(
                themeColor.copy(alpha = 0.7f),
                themeColor.copy(alpha = 0.4f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.5f),
                Color.White.copy(alpha = 0.2f)
            )
        )
    }
    
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .background(
                brush = backgroundGradient,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                brush = borderGradient,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
            if (showIcon) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GlassSkipButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .width(80.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = "Skip",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            letterSpacing = 0.5.sp
        )
    }
}
