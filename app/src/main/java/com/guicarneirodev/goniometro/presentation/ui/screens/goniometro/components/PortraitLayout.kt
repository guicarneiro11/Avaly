package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun PortraitLayout(viewModel: GoniometroScreenViewModel) {
    val currentImageUri by viewModel.currentImageUri
    val isLineSet by viewModel.isLineSet

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = AccentBlue.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            BackgroundImage(
                currentImageUri = currentImageUri,
                modifier = Modifier.fillMaxSize()
            )

            if (isLineSet) {
                GoniometroCanvas(
                    lineStart = viewModel.lineStart.value,
                    lineEnd = viewModel.lineEnd.value,
                    lines = viewModel.lines.value,
                    selectedAngleIndex = viewModel.selectedAngleIndex.value,
                    onLineStartChange = viewModel::setLineStart,
                    onLineEndChange = viewModel::setLineEnd,
                    onAddLine = viewModel::addLine,
                    onAngleChange = { angle ->
                        viewModel.updateCurrentAngle(angle)
                    }
                )
            }
        }

        ControlPanel(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = PrimaryLight.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(16.dp),
            viewModel = viewModel
        )
    }
}