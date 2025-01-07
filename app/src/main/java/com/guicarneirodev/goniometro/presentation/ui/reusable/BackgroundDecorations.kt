package com.guicarneirodev.goniometro.presentation.ui.reusable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun BackgroundDecorations() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val pattern = Path().apply {
            for (i in 0..size.width.toInt() step 40) {
                for (j in 0..size.height.toInt() step 40) {
                    moveTo(i.toFloat(), j.toFloat())
                    lineTo((i + 20).toFloat(), j.toFloat())
                }
            }
        }

        drawPath(
            path = pattern,
            color = PrimaryLight.copy(alpha = 0.03f),
            style = Stroke(width = 1f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AccentBlue.copy(alpha = 0.1f), Color.Transparent
                    )
                )
            )
    )
}