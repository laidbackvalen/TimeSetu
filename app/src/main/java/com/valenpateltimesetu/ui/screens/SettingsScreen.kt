package com.valenpateltimesetu.ui.screens

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import com.valenpateltimesetu.data.PreferencesManager
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    var showVersionDialog by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }

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
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            "Settings",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 20.dp,
                    bottom = innerPadding.calculateBottomPadding() + 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            // About Section
            item {
                SectionHeader("About")
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Settings,
                    title = "Version",
                    subtitle = "App version information",
                    iconColor = Color(0xFF6366F1),
                    onClick = { showVersionDialog = true }
                )
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Email,
                    title = "App Support",
                    subtitle = "User guide & help center",
                    iconColor = Color(0xFF10B981),
                    onClick = { navController.navigate("user_guide") }
                )
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Settings,
                    title = "Privacy Policy",
                    subtitle = "How we protect your data",
                    iconColor = Color(0xFF3B82F6),
                    onClick = { navController.navigate("privacy_policy") }
                )
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Edit,
                    title = "Terms & Conditions",
                    subtitle = "Terms of service",
                    iconColor = Color(0xFF8B5CF6),
                    onClick = { navController.navigate("terms_conditions") }
                )
            }
            
            // Engagement Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("Engagement")
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Info,
                    title = "Show Tutorial",
                    subtitle = "Learn how to use the app",
                    iconColor = themeColor,
                    onClick = {
                        preferencesManager.resetTutorial()
                        // Navigate to home - SideEffect will check preference and show tutorial
                        navController.navigate("home") {
                            popUpTo("settings") { inclusive = false }
                            launchSingleTop = false // Ensure home recomposes
                        }
                    }
                )
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Star,
                    title = "Rate Us",
                    subtitle = "Love the app? Rate us on Play Store",
                    iconColor = Color(0xFFF59E0B),
                    onClick = { openPlayStore(context) }
                )
            }
            
            item {
                ModernSettingItem(
                    icon = Icons.Default.Star,
                    title = "Feedback",
                    subtitle = "Share your thoughts & suggestions",
                    iconColor = themeColor,
                    onClick = { showFeedbackDialog = true }
                )
            }
            
            // Footer
            item {
                Spacer(modifier = Modifier.height(16.dp))
                ModernFooterCard()
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        
            // Version Dialog
        if (showVersionDialog) {
            VersionDialog(
                onDismiss = { showVersionDialog = false },
                context = context
            )
        }
        
        // Feedback Dialog
        if (showFeedbackDialog) {
            FeedbackDialog(
                onDismiss = { showFeedbackDialog = false },
                context = context
            )
        }
        }
    }
}

@Composable
fun VersionDialog(onDismiss: () -> Unit, context: Context) {
    val versionName = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        "1.0"
    }
    
    val versionCode = try {
        context.packageManager.getPackageInfo(context.packageName, 0).longVersionCode.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        "1"
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(12.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // App Icon/Logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(themeColor, themeColor.copy(alpha = 0.7f))
                                )
                            )
                            .border(3.dp, Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⏳", fontSize = 40.sp)
                    }
                    
                    // App Name
                    Text(
                        text = "TimeSetu",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    // Version Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .border(1.dp, themeColor.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Version $versionName ($versionCode)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = themeColor
                        )
                    }
                    
                    Divider(
                        color = Color.White.copy(alpha = 0.1f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    // Company Info
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Crafted by Nullscape",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                        
                        Text(
                            text = "www.nullscape.in",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Normal
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Close Button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColor
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = "Close",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackDialog(onDismiss: () -> Unit, context: Context) {
    var feedbackText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var isSending by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .shadow(12.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
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
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(themeColor, themeColor.copy(alpha = 0.7f))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                text = "Send Feedback",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    
                    Divider(color = Color.White.copy(alpha = 0.1f), thickness = 1.dp)
                    
                    // Star Rating Section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Rate your experience",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            for (i in 1..5) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (i <= rating) {
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        themeColor.copy(alpha = 0.3f),
                                                        Color.Transparent
                                                    )
                                                )
                                            } else {
                                                Brush.radialGradient(
                                                    colors = listOf(
                                                        Color.White.copy(alpha = 0.05f),
                                                        Color.Transparent
                                                    )
                                                )
                                            }
                                        )
                                        .border(
                                            1.dp,
                                            if (i <= rating) themeColor.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f),
                                            CircleShape
                                        )
                                        .clickable { rating = i },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "$i stars",
                                        tint = if (i <= rating) themeColor else Color.Gray.copy(alpha = 0.4f),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                        
                        if (rating > 0) {
                            Text(
                                text = when (rating) {
                                    1 -> "Poor"
                                    2 -> "Fair"
                                    3 -> "Good"
                                    4 -> "Very Good"
                                    5 -> "Excellent"
                                    else -> ""
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = themeColor
                            )
                        }
                    }
                    
                    // Feedback Text Field
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        placeholder = {
                            Text(
                                "Enter your feedback, suggestions, or report bugs...",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = themeColor,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                            focusedContainerColor = Color.White.copy(alpha = 0.05f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.03f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        maxLines = 5,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    )
                    
                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White.copy(alpha = 0.8f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                Color.White.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Cancel",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        
                        Button(
                            onClick = {
                                isSending = true
                                coroutineScope.launch {
                                    val success = sendFeedbackDirectly(context, feedbackText, rating)
                                    isSending = false
                                    if (success) {
                                        feedbackText = ""
                                        rating = 0
                                        onDismiss()
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            enabled = (feedbackText.isNotBlank() || rating > 0) && !isSending,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = themeColor,
                                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                pressedElevation = 2.dp,
                                disabledElevation = 0.dp
                            )
                        ) {
                            if (isSending) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        "Send",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 4.dp)
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

fun openAppSupport(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf("we.nullscape@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "TimeSetu - App Support")
        putExtra(Intent.EXTRA_TEXT, "Hello Nullscape Team,\n\n")
    }
    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    } catch (e: Exception) {
        // Handle error if no email app is available
    }
}

fun openPlayStore(context: Context) {
    try {
        val packageName = context.packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        context.startActivity(intent)
    } catch (e: Exception) {
        // If Play Store app is not available, open in browser
        try {
            val packageName = context.packageName
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
            context.startActivity(intent)
        } catch (e2: Exception) {
            // Handle error
        }
    }
}

suspend fun sendFeedbackDirectly(context: Context, feedback: String, rating: Int = 0): Boolean {
    return withContext(Dispatchers.Main) {
        // TODO: Add your backend email sending logic here
        // For now, just simulate success
        android.widget.Toast.makeText(context, "Feedback received! Thank you.", android.widget.Toast.LENGTH_SHORT).show()
        true
    }
}


@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        color = Color.White.copy(alpha = 0.6f),
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ModernSettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                iconColor.copy(alpha = 0.2f),
                                iconColor.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(1.dp, iconColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ModernFooterCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
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
                            themeColor.copy(alpha = 0.1f),
                            Color(0xFF1E1E1E),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Crafted by Nullscape",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    ),
                    textAlign = TextAlign.Center
                )
                
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "🇮🇳",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Made in India",
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            letterSpacing = 0.6.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
