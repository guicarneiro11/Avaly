package com.guicarneirodev.goniometro.presentation.ui.screens.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "Fade Animation"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "Scale Animation"
    )

    val rotation = rememberInfiniteTransition(label = "Shine Rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Rotation Animation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1500)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        SecondaryDark,
                        SecondaryDark.copy(alpha = 0.8f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .alpha(alphaAnim.value)
                    .scale(scaleAnim.value)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotation.value)
                        .background(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    AccentBlue.copy(alpha = 0.3f),
                                    AccentBlue.copy(alpha = 0.1f),
                                    AccentBlue.copy(alpha = 0f),
                                    AccentBlue.copy(alpha = 0.3f)
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(
                            color = PrimaryLight.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            spotColor = PrimaryLight.copy(alpha = 0.2f)
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .border(
                            width = 2.dp,
                            color = AccentBlue.copy(alpha = 0.4f),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .background(
                            color = SecondaryDark.copy(alpha = 0.9f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avaly_logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}