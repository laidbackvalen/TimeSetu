package com.valenpateltimesetu.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

data class OnboardingSlide(
    val title: String,
    val subtitle: String,
    val description: String,
    val backgroundGradient: List<Color>
)

val onboardingSlides = listOf(
    OnboardingSlide(
        title = "Welcome to TimeSetu",
        subtitle = "Productivity Redefined",
        description = "Master your time with precision. Focus deeply, work efficiently, achieve consistently.",
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
        // Skip button in top right corner
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 50.dp, end = 24.dp)
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
            modifier = Modifier.fillMaxSize()
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
                .offset(y = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(onboardingSlides.size) { index ->
                    val pageOffset = abs((currentPage + currentPageOffset) - index)
                    val isActive = pageOffset < 0.5f
                    
                    Box(
                        modifier = Modifier
                            .size(if (isActive) 10.dp else 8.dp)
                            .alpha(if (isActive) 1f else 0.4f)
                            .background(
                                color = if (isActive) themeColor else Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
        
        // Next/Get Started button at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
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
    
    Column(
        modifier = modifier
            .alpha(alpha)
            .offset(x = horizontalOffset.dp)
            .padding(horizontal = 48.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = slide.title,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            lineHeight = 45.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        
        // Subtitle
        Text(
            text = slide.subtitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        
        // Description
        Text(
            text = slide.description,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.8f),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            letterSpacing = 0.3.sp,
            modifier = Modifier.fillMaxWidth(0.85f).padding(bottom = 150.dp)
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

