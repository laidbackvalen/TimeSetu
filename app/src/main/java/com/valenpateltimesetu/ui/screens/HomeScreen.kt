package com.valenpateltimesetu.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var totalTime by remember { mutableStateOf(25 * 60 * 1000L) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000)
    )
    val offsetX: Dp by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else (-300).dp,
        animationSpec = tween(durationMillis = 1000)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "bounceAnim")
    val bounceScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounceScale"
    )


    LaunchedEffect(isRunning) {
        startAnimation = true
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1000
        }
        if (timeLeft <= 0) isRunning = false
    }

    val circleSize = 250.dp
    val strokeWidth = 8f
    val radiusPx = with(LocalDensity.current) { (circleSize / 2).toPx() }
    val modes = listOf(
        "5 min" to 5 * 60 * 1000L,
        "25 min" to 25 * 60 * 1000L,
        "15 min" to 15 * 60 * 1000L
    )
    var selectedIndex by remember { mutableStateOf(1) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .alpha(alphaAnim)
            .scale(scaleAnim)
            .background(color = backgroundColor)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .offset(offsetX)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            itemsIndexed(modes) { index, (label, time) ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (index == selectedIndex) Color(0xFFFF8C42) else Color.DarkGray
                        )
                        .clickable {
                            selectedIndex = index
                            totalTime = time
                            timeLeft = time
                            isRunning = false
                        }
                        .then(
                            if (label.startsWith("5") || label.startsWith("15"))
                                Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            else
                                Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color.White,
                        fontSize = if (label.startsWith("5") || label.startsWith("15")) 14.sp else 16.sp
                    )
                }
            }
        }

        Box(contentAlignment = Alignment.Center, modifier = Modifier) {
            Canvas(
                modifier = Modifier
                    .size(circleSize)
                    .scale(if (!isRunning) bounceScale else 1f)
                    .pointerInput(true) {
                        detectDragGestures { change, _ ->
                            val center = Offset(
                                (size.width / 2).toFloat(),
                                (size.height / 2).toFloat()
                            )
                            val touch = change.position
                            val angle =
                                atan2(touch.y - center.y, touch.x - center.x) + PI / 2
                            val correctedAngle =
                                if (angle < 0) angle + 2 * PI else angle
                            val progress = correctedAngle / (2 * PI)
                            timeLeft = (totalTime * progress).toLong()
                        }
                    }
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(
                    color = Color.Black,
                    radius = radiusPx
                )
                val sweepAngle = (timeLeft.toFloat() / totalTime) * 360f
                drawArc(
                    color = Color(0xFFFF8C42),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radiusPx, center.y - radiusPx),
                    size = Size(radiusPx * 2, radiusPx * 2),
                    style = Stroke(width = 20f)
                )

                val elapsedAngle = 360f - sweepAngle
                drawArc(
                    color = Color.Black,
                    startAngle = -90f + sweepAngle,
                    sweepAngle = elapsedAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radiusPx, center.y - radiusPx),
                    size = Size(radiusPx * 2, radiusPx * 2),
                    style = Stroke(width = strokeWidth)
                )

                val handleAngle = Math.toRadians(sweepAngle.toDouble() - 90)
                val handleX = center.x + radiusPx * cos(handleAngle).toFloat()
                val handleY = center.y + radiusPx * sin(handleAngle).toFloat()
                drawCircle(
                    color = Color(0xFFFF8C42),
                    radius = 30f,
                    center = Offset(handleX, handleY)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "TimeSetu⏳",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = Color.DarkGray)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    fontFamily = FontFamily.SansSerif
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${(timeLeft / 1000) / 60}:${
                        (timeLeft / 1000 % 60).toString().padStart(2, '0')
                    }",
                    color = Color.White,
                    fontSize = 40.sp,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .offset(offsetX)
                .height(60.dp)
                .padding(end = 16.dp, top = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Play Button
            AnimatedVisibility(
                visible = !isRunning,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF111111))
                        .clickable { isRunning = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Start",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = isRunning,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF111111))
                        .clickable { isRunning = false },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Pause",
                        tint = themeColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF111111))
                    .clickable { timeLeft = totalTime; isRunning = false },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
