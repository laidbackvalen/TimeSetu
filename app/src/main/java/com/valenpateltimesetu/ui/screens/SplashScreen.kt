package com.valenpateltimesetu.ui.screens

    import androidx.compose.animation.core.animateDpAsState
    import androidx.compose.animation.core.animateFloatAsState
    import androidx.compose.animation.core.tween
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.offset
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.alpha
    import androidx.compose.ui.draw.scale
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.navigation.NavHostController
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.Dp
    import com.valenpateltimesetu.ui.quotes.splashQuotes
    import kotlinx.coroutines.delay

    @Composable
    fun SplashScreen(navController: NavHostController) {
        val randomQuotes by remember { mutableStateOf(splashQuotes.random()) }
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
        LaunchedEffect(Unit) {
            startAnimation = true
            delay(4000)
            navController.navigate("home"){
                popUpTo("splash"){inclusive=true}
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFFF8C42))
                .alpha(alphaAnim)
                .scale(scaleAnim),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.padding(top = 100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(modifier = Modifier.offset(x = offsetX),
                        text="Minimal Distraction, Maximum Results!",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        lineHeight = 35.sp
                    )
//                    Spacer(modifier = Modifier.height(5.dp))
                    Text(modifier = Modifier.offset(x = offsetX),
                        text ="Master your time with the Pomodoro method.",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                }
                Text(
                    modifier = Modifier.padding(bottom = 60.dp).offset(x = offsetX),
                    text = randomQuotes,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                )
            }
        }
    }
