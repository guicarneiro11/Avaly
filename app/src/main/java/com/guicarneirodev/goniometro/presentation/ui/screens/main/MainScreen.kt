package com.guicarneirodev.goniometro.presentation.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.GoniometroScreen
import com.guicarneirodev.goniometro.ui.theme.GoniometroTheme

@Composable
fun MainScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: throw IllegalStateException("Usuário não está logado.")

    GoniometroTheme {
        Scaffold(
            topBar = { GoniometroScreen(navController, userId) },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = Color.Transparent
            ) {
                Background()
            }
        }
    }
}

@Composable
fun Background() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF2B6EA8),
                    Color(0xFF2B6EA8)
                )
            )
        )
    )
}