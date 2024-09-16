package com.guicarneirodev.goniometro

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel()
    val offsetY by homeViewModel.offsetY.collectAsState()

    HomeBackground {
        DraggableContent(
            offsetY = offsetY,
            onDrag = { dragAmount -> homeViewModel.updateOffsetY(dragAmount) },
            onDragEnd = { homeViewModel.resetOffsetY() }
        ) {
            HomeContent(navController)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2B6EA8), Color(0xFF2B6EA8))
                )
            )
            .semantics { testTagsAsResourceId = true }
            .testTag("homeBackground")
    ) {
        content()
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
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
            .offset(y = animatedOffsetY)
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
fun HomeContent(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent, shape = RoundedCornerShape(12.dp))
                .padding(16.dp)
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            WelcomeText()
            Spacer(modifier = Modifier.height(225.dp))
            LoginButton(navController)
            Spacer(modifier = Modifier.height(16.dp))
            RegisterButton(navController)
        }
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
        lineHeight = 40.sp
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

class HomeViewModel : ViewModel() {
    private val _offsetY = MutableStateFlow(0f)
    val offsetY: StateFlow<Float> = _offsetY.asStateFlow()

    fun updateOffsetY(dragAmount: Float) {
        _offsetY.value += dragAmount
    }

    fun resetOffsetY() {
        _offsetY.value = 0f
    }
}