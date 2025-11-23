package com.valenpateltimesetu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import com.valenpateltimesetu.ui.theme.backgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Black,
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
                items(listOf("Version", "App Support", "Privacy Policy", "Terms & Condition", "Rate Us", "Feedback")) { setting ->
                    SettingItem(setting)
                }
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
    }
}
@Composable
fun SettingItem(title: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        color = Color.DarkGray,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        onClick = { /* Handle click */ }
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
