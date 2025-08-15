package com.timesetu.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.timesetu.ui.theme.backgroundColor
import kotlinx.coroutines.delay
import kotlin.math.*
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var totalTime by remember { mutableStateOf(25 * 60 * 1000L) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
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
            .background(color = backgroundColor)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
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
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        
        Box(contentAlignment = Alignment.Center, modifier = Modifier) {
            Canvas(
                modifier = Modifier
                    .size(circleSize)
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
            Text(
                text = "${(timeLeft / 1000) / 60}:${
                    (timeLeft / 1000 % 60).toString().padStart(2, '0')
                }",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black)
                .height(60.dp)
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp,  Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF050505))
                    .clickable { isRunning = !isRunning },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Filled.Close else Icons.Filled.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF050505))
                    .clickable { timeLeft = totalTime; isRunning = false },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Reset",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
