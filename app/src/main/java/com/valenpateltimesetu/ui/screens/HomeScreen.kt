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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.derivedStateOf
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun ScrollableNumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (value - range.first).coerceIn(0, range.count() - 1)
    )
    val coroutineScope = rememberCoroutineScope()
    
    var isScrolling by remember { mutableStateOf(false) }
    var isUpdatingScroll by remember { mutableStateOf(false) }
    var lastKnownValue by remember { mutableStateOf(value) }
    var centeredValue by remember { mutableStateOf(value) }
    val density = LocalDensity.current
    val contentPaddingTopPx = with(density) { 70.dp.toPx() }
    
    // Update centered value continuously during scroll for real-time updates
    LaunchedEffect(Unit) {
        while (true) {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isNotEmpty()) {
                // Center Y of viewport (where orange highlight box is positioned)
                // The orange box is at the center of the 200dp Box = 100dp from top
                val centerY = layoutInfo.viewportSize.height / 2f
                
                // Find the item whose center is closest to the viewport center
                // item.offset in visibleItemsInfo is relative to the content start (after padding)
                // We need to add the padding offset to compare with viewport center
                val centeredItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
                    // item.offset is from content start, add padding to get viewport position
                    val itemCenter = item.offset + contentPaddingTopPx + (item.size / 2f)
                    kotlin.math.abs(itemCenter - centerY)
                }
                
                val index = (centeredItem?.index ?: listState.firstVisibleItemIndex).coerceIn(0, range.count() - 1)
                val newCenteredValue = (range.first + index).coerceIn(range)
                if (newCenteredValue != centeredValue) {
                    centeredValue = newCenteredValue
                }
            }
            kotlinx.coroutines.delay(16) // Update ~60fps for smooth updates
        }
    }
    
    // Update value when scrolling stops
    LaunchedEffect(listState.isScrollInProgress) {
        isScrolling = listState.isScrollInProgress
        if (!listState.isScrollInProgress && !isUpdatingScroll) {
            kotlinx.coroutines.delay(250)
            if (!listState.isScrollInProgress && !isUpdatingScroll) {
                // First, snap to center
                val layoutInfo = listState.layoutInfo
                // The orange box is at the center of the 200dp Box = 100dp from top
                val centerY = layoutInfo.viewportSize.height / 2f
                // item.offset is relative to content start, add padding to get viewport position
                val centeredItem = layoutInfo.visibleItemsInfo.minByOrNull { item ->
                    val itemCenter = item.offset + contentPaddingTopPx + (item.size / 2f)
                    kotlin.math.abs(itemCenter - centerY)
                }
                val targetIndex = (centeredItem?.index ?: listState.firstVisibleItemIndex).coerceIn(0, range.count() - 1)
                if (targetIndex != listState.firstVisibleItemIndex) {
                    isUpdatingScroll = true
                    coroutineScope.launch {
                        listState.animateScrollToItem(targetIndex)
                        kotlinx.coroutines.delay(400)
                        // After snap, update value to match what's centered
                        val finalValue = centeredValue
                        if (finalValue != value && finalValue in range) {
                            lastKnownValue = finalValue
                            onValueChange(finalValue)
                        }
                        isUpdatingScroll = false
                    }
                } else {
                    // Already centered, just update value
                    val finalValue = centeredValue
                    if (finalValue != value && finalValue in range) {
                        lastKnownValue = finalValue
                        onValueChange(finalValue)
                    }
                }
            }
        }
    }
    
    // Scroll to value when it changes externally (only on initial load or external change)
    LaunchedEffect(value) {
        // Only react if value changed externally (not from our scroll stop update)
        if (!isUpdatingScroll && !isScrolling && value != lastKnownValue) {
            val targetIndex = (value - range.first).coerceIn(0, range.count() - 1)
            val currentIndex = listState.firstVisibleItemIndex
            if (currentIndex != targetIndex) {
                isUpdatingScroll = true
                coroutineScope.launch {
                    listState.animateScrollToItem(targetIndex)
                    kotlinx.coroutines.delay(300)
                    isUpdatingScroll = false
                    lastKnownValue = value
                }
            } else {
                lastKnownValue = value
            }
        }
    }
    
    Box(
        modifier = modifier
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2A2A2A)),
        contentAlignment = Alignment.Center
    ) {
        // Selection highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .background(
                    color = themeColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                )
        )
        
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // Improve touch handling for smoother scrolling
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 70.dp),
            userScrollEnabled = true
        ) {
            items(range.count()) { index ->
                val number = range.first + index
                // Always show the centered item as selected (bold) - this is the present number in the center
                val isSelected = number == centeredValue
                
                Text(
                    text = number.toString().padStart(2, '0'),
                    fontSize = if (isSelected) 40.sp else 28.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .clickable { 
                            onValueChange(number)
                            isUpdatingScroll = true
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                                kotlinx.coroutines.delay(300)
                                isUpdatingScroll = false
                            }
                        }
                        .padding(vertical = 10.dp),
                    style = TextStyle(
                        fontSize = if (isSelected) 40.sp else 28.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var totalTime by remember { mutableStateOf(25 * 60 * 1000L) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var minutesInput by remember { mutableStateOf(0) }
    var secondsInput by remember { mutableStateOf(0) }

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

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1000
        }
        if (timeLeft <= 0) isRunning = false
    }

    val circleSize = 300.dp
    val strokeWidth = 15f
    val radiusPx = with(LocalDensity.current) { (circleSize / 2).toPx() }
    val modes = listOf(
        "5 min" to 5 * 60 * 1000L,
        "25 min" to 25 * 60 * 1000L,
        "15 min" to 15 * 60 * 1000L
    )
    var selectedIndex by remember { mutableStateOf(1) }
    
    // Check if current time is custom (doesn't match any predefined mode)
    val isCustomTime = !modes.any { it.second == totalTime }
    
    // Sync selectedIndex when totalTime changes to match a predefined mode
    LaunchedEffect(totalTime) {
        val matchingIndex = modes.indexOfFirst { it.second == totalTime }
        if (matchingIndex >= 0) {
            selectedIndex = matchingIndex
        } else {
            selectedIndex = -1
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .alpha(alphaAnim)
            .scale(scaleAnim)
            .statusBarsPadding()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .offset(offsetX)
                .padding(top = 20.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(horizontal = 40.dp)
        ) {
            itemsIndexed(modes) { index, (label, time) ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (!isCustomTime && index == selectedIndex) Color(0xFFFF8C42) else Color.DarkGray
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
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
                                val distanceFromCenter = sqrt(
                                    (touch.x - center.x).pow(2) + (touch.y - center.y).pow(2)
                                )
                                // Only allow dragging when touch is near the ring (not in center)
                                // Ring is at radiusPx, so allow touches between 80% and 120% of radius
                                val minRadius = radiusPx * 0.8f
                                val maxRadius = radiusPx * 1.2f
                                if (distanceFromCenter >= minRadius && distanceFromCenter <= maxRadius) {
                                    val angle =
                                        atan2(touch.y - center.y, touch.x - center.x) + PI / 2
                                    val correctedAngle =
                                        if (angle < 0) angle + 2 * PI else angle
                                    val progress = correctedAngle / (2 * PI)
                                    timeLeft = (totalTime * progress).toLong()
                                }
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
                    Box(
                        modifier = Modifier
                            .clickable {
                                minutesInput = ((timeLeft / 1000) / 60).toInt()
                                secondsInput = ((timeLeft / 1000) % 60).toInt()
                                showEditDialog = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${(timeLeft / 1000) / 60}:${
                                    (timeLeft / 1000 % 60).toString().padStart(2, '0')
                                }",
                                color = Color.White,
                                fontSize = 60.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Time",
                                tint = Color(0xFFFF8C42),
                                modifier = Modifier
                                    .size(30.dp)
                                    .offset(y = 6.dp)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(50.dp))
            
            Row(
                modifier = Modifier
                    .offset(offsetX),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                            .size(60.dp)
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
                            .size(60.dp)
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
                        .size(60.dp)
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
    
    // Edit Time Dialog
    if (showEditDialog) {
        Dialog(
            onDismissRequest = { showEditDialog = false },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            AnimatedVisibility(
                visible = showEditDialog,
                enter = fadeIn(animationSpec = tween(200)) + scaleIn(
                    initialScale = 0.9f,
                    animationSpec = tween(200)
                ),
                exit = fadeOut(animationSpec = tween(150)) + scaleOut(
                    targetScale = 0.9f,
                    animationSpec = tween(150)
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable { showEditDialog = false },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .shadow(16.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1E1E1E)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // Title
                            Text(
                                text = "Set Custom Time",
                                fontSize = 24.sp,
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            
                            // Time Input Section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Minutes Input
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Minutes",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    ScrollableNumberPicker(
                                        value = minutesInput,
                                        onValueChange = { minutesInput = it },
                                        range = 0..59,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                
                                // Separator
                                Text(
                                    text = ":",
                                    fontSize = 32.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 24.dp)
                                )
                                
                                // Seconds Input
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Seconds",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    ScrollableNumberPicker(
                                        value = secondsInput,
                                        onValueChange = { secondsInput = it },
                                        range = 0..59,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            
                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { showEditDialog = false },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color.Gray
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        Color(0xFF404040)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                
                                Button(
                                    onClick = {
                                        val newTime = (minutesInput * 60 + secondsInput) * 1000L
                                        if (newTime > 0) {
                                            totalTime = newTime
                                            timeLeft = newTime
                                            isRunning = false
                                            // Reset selectedIndex to -1 if custom time doesn't match any mode
                                            val matchingIndex = modes.indexOfFirst { it.second == newTime }
                                            selectedIndex = if (matchingIndex >= 0) matchingIndex else -1
                                        }
                                        showEditDialog = false
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = themeColor,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 0.dp,
                                        pressedElevation = 2.dp
                                    )
                                ) {
                                    Text(
                                        "Set",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp)
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
