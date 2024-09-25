package com.guicarneirodev.goniometro.presentation.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("homeBackground")
            .semantics { testTagsAsResourceId = true }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2B6EA8), Color(0xFF2B6EA8))
                )
            )
    ) {
        content()
    }
}

@Composable
fun DraggableContent(
    offsetY: Float,
    onDrag: (Float) -> Unit,
    onDragEnd: () -> Unit,
    content: @Composable () -> Unit
) {
    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY.dp,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, animatedOffsetY.roundToPx()) }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() },
                    onVerticalDrag = { _, dragAmount -> onDrag(dragAmount) }
                )
            }
    ) {
        content()
    }
}

@Composable
fun WelcomeText() {
    Text(
        text = "Bem-vindo ao\nAngle Pro,\nsua goniometria\nem poucos cliques.",
        color = Color(0xFFFFFFFF),
        fontSize = 30.sp,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.Default,
        lineHeight = 40.sp,
        modifier = Modifier.testTag("welcomeText")
    )
}

@Composable
fun LoginButton(navController: NavController) {
    ActionButton(
        text = "Fazer Login",
        onClick = { navController.navigate("login") },
        testTag = "loginButton"
    )
}

@Composable
fun RegisterButton(navController: NavController) {
    ActionButton(
        text = "Criar Conta",
        onClick = { navController.navigate("register") },
        testTag = "registerButton"
    )
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit, testTag: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag(testTag)
        ) {
            Text(
                text = text,
                color = Color(0xFF2B6EA8),
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}