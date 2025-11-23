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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.valenpateltimesetu.ui.theme.backgroundColor
import com.valenpateltimesetu.ui.theme.themeColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    var showVersionDialog by remember { mutableStateOf(false) }
    var showFeedbackDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
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
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 0.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        "About",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                item { SettingItem("Version") { showVersionDialog = true } }
                item { SettingItem("App Support") { navController.navigate("user_guide") } }
                item { 
                    SettingItem("Privacy Policy") { 
                        navController.navigate("privacy_policy")
                    }
                }
                item { 
                    SettingItem("Terms & Condition") { 
                        navController.navigate("terms_conditions")
                    }
                }
                item { SettingItem("Rate Us") { openPlayStore(context) } }
                item { SettingItem("Feedback") { showFeedbackDialog = true } }
            }
            
            // Footer at the bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                    Text(
                        text = "Crafted by Nullscape",
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🇮🇳",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                        Text(
                            text = "Made in India",
                            style = TextStyle(
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 12.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                }
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
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "TimeSetu",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = "Version $versionName ($versionCode)",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                
                Divider(color = Color.Gray.copy(alpha = 0.3f))
                
                Text(
                    text = "Crafted by Nullscape",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Text(
                    text = "www.nullscape.in",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = themeColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Close",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
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
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Send Feedback",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                // Star Rating
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Rate your experience",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$i stars",
                                tint = if (i <= rating) themeColor else Color.Gray.copy(alpha = 0.4f),
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        rating = i
                                    }
                            )
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
                            fontSize = 12.sp,
                            color = themeColor
                        )
                    }
                }
                
                Divider(color = Color.Gray.copy(alpha = 0.3f))
                
                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    placeholder = { Text("Enter your feedback, suggestions, or report bugs...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = themeColor,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 5
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
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
                            modifier = Modifier.padding(vertical = 8.dp)
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
                        modifier = Modifier.weight(1f),
                        enabled = feedbackText.isNotBlank() || rating > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = themeColor,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isSending) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Send",
                                color = Color.White,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
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
fun SettingItem(title: String, onClick: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = Color.DarkGray,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                color = Color.White
            )
        }
    }
}
