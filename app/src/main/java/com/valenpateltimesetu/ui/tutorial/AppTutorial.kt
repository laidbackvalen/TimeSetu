package com.valenpateltimesetu.ui.tutorial

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.valenpateltimesetu.ui.theme.themeColor

data class TutorialStep(
    val title: String,
    val description: String,
    val targetPosition: Offset? = null,
    val targetSize: Size? = null
)

@Composable
fun TutorialOverlay(
    steps: List<TutorialStep>,
    currentStep: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Log.d("TutorialOverlay", "TutorialOverlay called: currentStep=$currentStep, stepsSize=${steps.size}")
    
    if (currentStep >= steps.size) {
        Log.d("TutorialOverlay", "Returning early: currentStep >= steps.size")
        return
    }
    
    val step = steps[currentStep]
    Log.d("TutorialOverlay", "Step title: ${step.title}, position: ${step.targetPosition}, size: ${step.targetSize}")
    val density = LocalDensity.current
    
    // Animation for spotlight effect
    val infiniteTransition = rememberInfiniteTransition(label = "spotlight")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .zIndex(1000f) // Ensure it's on top
    ) {
        val screenHeightPx = with(density) { maxHeight.toPx() }
        
        Log.d("TutorialOverlay", "BoxWithConstraints: maxHeight=${maxHeight}, screenHeightPx=$screenHeightPx")
        
        // Dark overlay with cutout
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            
            Log.d("TutorialOverlay", "Canvas drawing: width=$width, height=$height")
            
            // Draw dark overlay
            drawRect(
                color = Color.Black.copy(alpha = 0.75f),
                size = size
            )
            
            // Create cutout for highlighted element (only if position is available)
            step.targetPosition?.let { pos ->
                step.targetSize?.let { targetSize ->
                    Log.d("TutorialOverlay", "Drawing cutout: pos=$pos, size=$targetSize, width=$width, height=$height")
                    // Ensure position is within reasonable bounds (allow some negative for off-screen elements)
                    if (targetSize.width > 0 && targetSize.height > 0) {
                        // Calculate center of target element
                        val centerX = pos.x + targetSize.width / 2
                        val centerY = pos.y + targetSize.height / 2
                        val radius = maxOf(targetSize.width, targetSize.height) / 2 + 20f
                        
                        Log.d("TutorialOverlay", "Cutout center: ($centerX, $centerY), radius: $radius")
                        
                        // Only draw if center is within screen bounds (with some tolerance)
                        if (centerX > -radius && centerX < width + radius && 
                            centerY > -radius && centerY < height + radius) {
                            val path = Path().apply {
                                addRect(androidx.compose.ui.geometry.Rect(0f, 0f, width, height))
                                // Cutout circle
                                addOval(
                                    androidx.compose.ui.geometry.Rect(
                                        centerX - radius,
                                        centerY - radius,
                                        centerX + radius,
                                        centerY + radius
                                    )
                                )
                            }
                            
                            clipPath(path) {
                                drawRect(Color.Transparent)
                            }
                            
                            // Highlight ring
                            drawCircle(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        themeColor.copy(alpha = pulseAlpha),
                                        themeColor.copy(alpha = 0.0f)
                                    ),
                                    radius = radius
                                ),
                                radius = radius,
                                center = Offset(centerX, centerY)
                            )
                        } else {
                            Log.d("TutorialOverlay", "Cutout center out of bounds, skipping")
                        }
                    }
                }
            } ?: run {
                Log.d("TutorialOverlay", "No target position available, showing full overlay")
            }
        }
        
        // Tutorial content card - positioned based on target location
        Column(
            modifier = Modifier
                .align(
                    when {
                        step.targetPosition == null -> Alignment.Center
                        step.targetPosition.y < screenHeightPx / 2 -> Alignment.BottomCenter
                        else -> Alignment.TopCenter
                    }
                )
                .padding(24.dp)
                .fillMaxWidth(0.9f)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.15f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Skip button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Skip",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onSkip() }
                )
            }
            
            // Title
            Text(
                text = step.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            // Description
            Text(
                text = step.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
            
            // Step indicator
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(steps.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentStep) 10.dp else 6.dp)
                            .background(
                                color = if (index == currentStep) themeColor else Color.White.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                    )
                }
            }
            
            // Next button
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
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
                        text = if (currentStep < steps.size - 1) "Next" else "Got it!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    if (currentStep < steps.size - 1) {
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
    }
}

@Composable
fun rememberTutorialState() = remember { mutableStateOf<TutorialState?>(null) }

data class TutorialState(
    val steps: List<TutorialStep>,
    val currentStep: Int = 0
)
