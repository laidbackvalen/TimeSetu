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
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.SideEffect
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import com.valenpateltimesetu.data.PreferencesManager
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor
import com.valenpateltimesetu.ui.tutorial.TutorialOverlay
import com.valenpateltimesetu.ui.tutorial.TutorialStep
import com.valenpateltimesetu.ui.tutorial.rememberTutorialState
import com.valenpateltimesetu.ui.navigation.drawer.AppDrawer
import androidx.navigation.compose.currentBackStackEntryAsState
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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.15f),
                        Color.White.copy(alpha = 0.08f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = themeColor.copy(alpha = 0.2f)
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        themeColor.copy(alpha = 0.4f),
                        Color.White.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Selection highlight
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            themeColor.copy(alpha = 0.3f),
                            themeColor.copy(alpha = 0.2f),
                            themeColor.copy(alpha = 0.15f)
                        )
                    ),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: androidx.navigation.NavController? = null
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val density = LocalDensity.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController?.currentBackStackEntryAsState()?.value?.destination?.route
    
    var totalTime by remember { mutableStateOf(25 * 60 * 1000L) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var isRunning by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) } // Start with false to trigger slide-in animation
    var showEditDialog by remember { mutableStateOf(false) }
    var minutesInput by remember { mutableStateOf(0) }
    var secondsInput by remember { mutableStateOf(0) }
    
    // Tutorial state - check if tutorial has been completed
    var showTutorial by remember { mutableStateOf(!preferencesManager.isTutorialCompleted()) }
    var tutorialStep by remember { mutableStateOf(0) }
    
    // Check tutorial status whenever the screen is recomposed
    // This ensures tutorial shows when returning from settings
    SideEffect {
        val shouldShow = !preferencesManager.isTutorialCompleted()
        if (shouldShow != showTutorial) {
            showTutorial = shouldShow
            if (shouldShow) {
                tutorialStep = 0 // Reset to first step when showing tutorial
            }
        }
    }
    var timerPosition by remember { mutableStateOf<Offset?>(null) }
    var timerSize by remember { mutableStateOf<Size?>(null) }
    var modeSelectorPosition by remember { mutableStateOf<Offset?>(null) }
    var modeSelectorSize by remember { mutableStateOf<Size?>(null) }
    var playButtonPosition by remember { mutableStateOf<Offset?>(null) }
    var playButtonSize by remember { mutableStateOf<Size?>(null) }
    
    // Debug logging
    LaunchedEffect(showTutorial) {
        Log.d("Tutorial", "showTutorial = $showTutorial")
        Log.d("Tutorial", "tutorialStep = $tutorialStep")
        Log.d("Tutorial", "isTutorialCompleted = ${preferencesManager.isTutorialCompleted()}")
    }
    
    // Ensure tutorial shows after layout is complete
    LaunchedEffect(showTutorial) {
        if (showTutorial) {
            Log.d("Tutorial", "Waiting for positions...")
            // Wait a bit for positions to be captured
            delay(800)
            Log.d("Tutorial", "Timer position: $timerPosition, size: $timerSize")
            Log.d("Tutorial", "Mode selector position: $modeSelectorPosition, size: $modeSelectorSize")
            Log.d("Tutorial", "Play button position: $playButtonPosition, size: $playButtonSize")
        }
    }
    
    val tutorialSteps = remember(timerPosition, timerSize, modeSelectorPosition, modeSelectorSize, playButtonPosition, playButtonSize) {
        val steps = listOf(
            TutorialStep(
                title = "Welcome to TimeSetu!",
                description = "This is your timer. You can drag the orange handle around the circle to adjust the time.",
                targetPosition = timerPosition,
                targetSize = timerSize
            ),
            TutorialStep(
                title = "Select Time Mode",
                description = "Choose from preset time modes (5 min, 25 min, 15 min) or create your own custom time.",
                targetPosition = modeSelectorPosition,
                targetSize = modeSelectorSize
            ),
            TutorialStep(
                title = "Start Your Session",
                description = "Tap the play button to start your Pomodoro session. Focus and stay productive!",
                targetPosition = playButtonPosition,
                targetSize = playButtonSize
            )
        )
        Log.d("Tutorial", "tutorialSteps created: size=${steps.size}, step0 position=${steps[0].targetPosition}, step1 position=${steps[1].targetPosition}, step2 position=${steps[2].targetPosition}")
        steps
    }

    val offsetX: Dp by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else (-300).dp,
        animationSpec = tween(durationMillis = 1000)
    )

    // Trigger slide-in animation on first load
    LaunchedEffect(Unit) {
        delay(100) // Small delay to ensure layout is ready
        startAnimation = true
    }

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
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft -= 1000
        }
        if (timeLeft <= 0) isRunning = false
    }

    val circleSize = 250.dp
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
    
    // Blur animation for dialog background
    val blurRadius by animateFloatAsState(
        targetValue = if (showEditDialog) 12f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "blurRadius"
    )
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (navController != null) {
                AppDrawer(
                    navController = navController,
                    currentRoute = currentRoute,
                    onClose = { scope.launch { drawerState.close() } }
                )
            }
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .blur(radius = blurRadius.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0A0F),
                            Color(0xFF1A1A2E),
                            Color(0xFF16213E),
                            backgroundColor
                        )
                    )
                )
                .statusBarsPadding()
        ) {
            // Drawer Menu Button
            IconButton(
                onClick = { scope.launch { drawerState.open() } },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .zIndex(10f)
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(offsetX)
                    .padding(top = 70.dp)
                    .align(Alignment.TopCenter)
                .onGloballyPositioned { coordinates ->
                    val position = coordinates.localToRoot(Offset.Zero)
                    val size = coordinates.size
                    modeSelectorPosition = position
                    modeSelectorSize = Size(size.width.toFloat(), size.height.toFloat())
                    Log.d("Tutorial", "Mode selector positioned: $position, size: ${size.width}x${size.height}")
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            contentPadding = PaddingValues(horizontal = 40.dp)
        ) {
            itemsIndexed(modes) { index, (label, time) ->
                val isSelected = !isCustomTime && index == selectedIndex
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = if (isSelected) {
                                    listOf(
                                        themeColor.copy(alpha = 0.4f),
                                        themeColor.copy(alpha = 0.6f)
                                    )
                                } else {
                                    listOf(
                                        Color.White.copy(alpha = 0.1f),
                                        Color.White.copy(alpha = 0.05f)
                                    )
                                }
                            ),
                            shape = CircleShape
                        )
                        .blur(radius = 0.dp)
                        .shadow(
                            elevation = if (isSelected) 12.dp else 4.dp,
                            shape = CircleShape,
                            spotColor = if (isSelected) themeColor.copy(alpha = 0.5f) else Color.Transparent
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            brush = Brush.linearGradient(
                                colors = if (isSelected) {
                                    listOf(
                                        Color.White.copy(alpha = 0.6f),
                                        themeColor.copy(alpha = 0.8f)
                                    )
                                } else {
                                    listOf(
                                        Color.White.copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                }
                            ),
                            shape = CircleShape
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
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.9f),
                        fontSize = if (label.startsWith("5") || label.startsWith("15")) 14.sp else 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            // Glassmorphic container for timer
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .onGloballyPositioned { coordinates ->
                        val position = coordinates.localToRoot(Offset.Zero)
                        val size = coordinates.size
                        timerPosition = position
                        timerSize = Size(size.width.toFloat(), size.height.toFloat())
                        Log.d("Tutorial", "Timer positioned: $position, size: ${size.width}x${size.height}")
                    }
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.White.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
                    .blur(radius = 0.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        spotColor = themeColor.copy(alpha = 0.3f)
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                themeColor.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.2f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
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
                    
                    // Glassmorphic background circle with gradient
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.5f)
                            ),
                            center = center,
                            radius = radiusPx
                        ),
                        radius = radiusPx
                    )
                    
                    // Outer glass border
                    drawCircle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                themeColor.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.2f)
                            )
                        ),
                        radius = radiusPx,
                        style = Stroke(width = 2f)
                    )
                    
                    val sweepAngle = (timeLeft.toFloat() / totalTime) * 360f
                    
                    // Glassmorphic progress arc with linear gradient
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.9f),
                                themeColor,
                                themeColor.copy(alpha = 0.8f)
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radiusPx, center.y - radiusPx),
                        size = Size(radiusPx * 2, radiusPx * 2),
                        style = Stroke(width = 20f)
                    )
                    
                    // Inner glow for progress arc
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.4f),
                                themeColor.copy(alpha = 0.2f),
                                Color.Transparent
                            )
                        ),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = Offset(center.x - radiusPx, center.y - radiusPx),
                        size = Size(radiusPx * 2, radiusPx * 2),
                        style = Stroke(width = 8f)
                    )

                    val elapsedAngle = 360f - sweepAngle
                    
                    // Glassmorphic elapsed arc
                    drawArc(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.15f),
                                Color.White.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.2f)
                            )
                        ),
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
                    
                    // Glassmorphic handle with glow
                    // Outer glow
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.3f),
                                Color.Transparent
                            ),
                            radius = 40f
                        ),
                        radius = 40f,
                        center = Offset(handleX, handleY)
                    )
                    
                    // Main handle circle
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 1f),
                                themeColor.copy(alpha = 0.8f),
                                themeColor.copy(alpha = 0.6f)
                            ),
                            radius = 30f
                        ),
                        radius = 30f,
                        center = Offset(handleX, handleY)
                    )
                    
                    // Inner highlight
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            radius = 15f
                        ),
                        radius = 15f,
                        center = Offset(handleX - 8f, handleY - 8f)
                    )
                }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "TimeSetu⏳",
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.2f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium
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
                            .onGloballyPositioned { coordinates ->
                                val position = coordinates.localToRoot(Offset.Zero)
                                val size = coordinates.size
                                playButtonPosition = position
                                playButtonSize = Size(size.width.toFloat(), size.height.toFloat())
                                Log.d("Tutorial", "Play button positioned: $position, size: ${size.width}x${size.height}")
                            }
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.25f),
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = themeColor.copy(alpha = 0.3f)
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.4f),
                                        Color.White.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = CircleShape
                            )
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
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        themeColor.copy(alpha = 0.3f),
                                        themeColor.copy(alpha = 0.15f)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .shadow(
                                elevation = 8.dp,
                                shape = CircleShape,
                                spotColor = themeColor.copy(alpha = 0.5f)
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        themeColor.copy(alpha = 0.6f),
                                        themeColor.copy(alpha = 0.4f)
                                    )
                                ),
                                shape = CircleShape
                            )
                            .clickable { isRunning = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Pause",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.25f),
                                    Color.White.copy(alpha = 0.1f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            spotColor = Color.White.copy(alpha = 0.2f)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.4f),
                                    Color.White.copy(alpha = 0.2f)
                                )
                            ),
                            shape = CircleShape
                        )
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
        
        // Tutorial overlay - show even if positions aren't available yet
        // Place it at the end to ensure it's on top of everything
        val shouldShowTutorial = showTutorial && tutorialStep < tutorialSteps.size
        
        SideEffect {
            Log.d("Tutorial", "SideEffect: shouldShowTutorial=$shouldShowTutorial (showTutorial=$showTutorial, tutorialStep=$tutorialStep, tutorialSteps.size=${tutorialSteps.size})")
        }
        
        LaunchedEffect(showTutorial, tutorialStep, tutorialSteps.size) {
            Log.d("Tutorial", "LaunchedEffect: Checking tutorial display: showTutorial=$showTutorial, step=$tutorialStep, stepsSize=${tutorialSteps.size}, condition=$shouldShowTutorial")
        }
        
        if (shouldShowTutorial) {
            SideEffect {
                Log.d("Tutorial", "Rendering TutorialOverlay with step $tutorialStep")
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1000f) // Ensure tutorial is on top
            ) {
                TutorialOverlay(
                    steps = tutorialSteps,
                    currentStep = tutorialStep,
                    onNext = {
                        if (tutorialStep < tutorialSteps.size - 1) {
                            tutorialStep++
                        } else {
                            showTutorial = false
                            preferencesManager.setTutorialCompleted(true)
                        }
                    },
                    onSkip = {
                        showTutorial = false
                        preferencesManager.setTutorialCompleted(true)
                    },
                    modifier = Modifier.fillMaxSize()
                )
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
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.8f),
                                    Color.Black.copy(alpha = 0.6f)
                                )
                            )
                        )
                        .clickable { showEditDialog = false },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .shadow(
                                elevation = 24.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = themeColor.copy(alpha = 0.3f)
                            )
                            .border(
                                width = 1.5.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.3f),
                                        themeColor.copy(alpha = 0.4f),
                                        Color.White.copy(alpha = 0.2f)
                                    )
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.15f),
                                            Color.White.copy(alpha = 0.08f),
                                            Color.White.copy(alpha = 0.05f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(24.dp)
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
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.1f),
                                                    Color.White.copy(alpha = 0.05f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .border(
                                            width = 1.5.dp,
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.3f),
                                                    Color.White.copy(alpha = 0.2f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable { showEditDialog = false }
                                ) {
                                    Text(
                                        "Cancel",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    themeColor.copy(alpha = 0.8f),
                                                    themeColor.copy(alpha = 0.6f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(12.dp),
                                            spotColor = themeColor.copy(alpha = 0.5f)
                                        )
                                        .border(
                                            width = 1.5.dp,
                                            brush = Brush.linearGradient(
                                                colors = listOf(
                                                    Color.White.copy(alpha = 0.5f),
                                                    themeColor.copy(alpha = 0.8f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
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
                                        }
                                ) {
                                    Text(
                                        "Set",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp),
                                        textAlign = TextAlign.Center
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
    }
}
