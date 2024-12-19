package com.guicarneirodev.goniometro.presentation.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.viewmodel.HomeScreenViewModel
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.DraggableContent
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.HomeContent
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.LogoSection

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeScreenViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5),
                        Color(0xFF4FC3F7)
                    )
                )
            )
    ) {
        BackgroundDecorations()

        DraggableContent(
            offsetY = uiState.offsetY,
            onDrag = { dragAmount -> viewModel.updateOffsetY(dragAmount) },
            onDragEnd = { viewModel.resetOffsetY() }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                LogoSection()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    HomeContent(navController)
                }
            }
        }
    }
}