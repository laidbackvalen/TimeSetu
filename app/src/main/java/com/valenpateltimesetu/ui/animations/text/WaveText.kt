package com.valenpateltimesetu.ui.animations.text

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun WaveText(
    text: String,
    startAnimation: Boolean,
    color: Color = Color.White,
    fontSize: Int = 30,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Row {
        text.forEachIndexed { index, char ->
            val offsetY: Dp by animateDpAsState(
                targetValue = if (startAnimation) 0.dp else 30.dp,
                animationSpec = tween(durationMillis = 500, delayMillis = index * 100)
            )
            Text(
                text = char.toString(),
                color = color,
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.offset(y = offsetY).padding(bottom = 60.dp)
            )
        }
    }
}
