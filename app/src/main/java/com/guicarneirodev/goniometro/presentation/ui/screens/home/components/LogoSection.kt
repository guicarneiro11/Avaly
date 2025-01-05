package com.guicarneirodev.goniometro.presentation.ui.screens.home.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun LogoSection() {
    val rotation = rememberInfiniteTransition(label = "Logo Rotation Animation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Logo Shine Rotation"
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation.value)
                .background(
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            AccentBlue.copy(alpha = 0.2f),
                            AccentBlue.copy(alpha = 0.05f),
                            AccentBlue.copy(alpha = 0f),
                            AccentBlue.copy(alpha = 0.2f)
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = PrimaryLight.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = PrimaryLight.copy(alpha = 0.1f)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .border(
                    width = 1.dp,
                    color = AccentBlue.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(
                    color = SecondaryDark.copy(alpha = 0.7f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avaly_logo),
                contentDescription = "Avaly Logo",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }
    }
}