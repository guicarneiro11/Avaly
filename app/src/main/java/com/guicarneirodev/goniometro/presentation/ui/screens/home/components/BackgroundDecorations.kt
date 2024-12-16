package com.guicarneirodev.goniometro.presentation.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.blur

@Composable
fun BackgroundDecorations() {
    Box(
        modifier = Modifier
            .size(600.dp)
            .offset(x = 200.dp, y = (-200).dp)
            .background(
                color = Color.White.copy(alpha = 0.1f),
                shape = CircleShape
            )
            .blur(radius = 70.dp)
    )

    // Círculo inferior esquerdo
    Box(
        modifier = Modifier
            .size(400.dp)
            .offset(x = (-100).dp, y = 600.dp)
            .background(
                color = Color.White.copy(alpha = 0.1f),
                shape = CircleShape
            )
            .blur(radius = 50.dp)
    )
}
