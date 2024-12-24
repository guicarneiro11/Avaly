package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel

@Composable
fun ImageWithGoniometer(viewModel: GoniometroScreenViewModel) {
    val currentImageUri by viewModel.currentImageUri
    val isLineSet by viewModel.isLineSet

    Box(modifier = Modifier.fillMaxSize()) {
        currentImageUri?.let {
            Log.d("ImageWithGoniometer", "Tentando carregar imagem: $it")
            BackgroundImage(currentImageUri)
        }

        Box(
            contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()
        ) {
            if (isLineSet) {
                GoniometroCanvas(lineStart = viewModel.lineStart.value,
                    lineEnd = viewModel.lineEnd.value,
                    lines = viewModel.lines.value,
                    selectedAngleIndex = viewModel.selectedAngleIndex.value,
                    onLineStartChange = viewModel::setLineStart,
                    onLineEndChange = viewModel::setLineEnd,
                    onAddLine = viewModel::addLine,
                    onAngleChange = { angle ->
                        viewModel.updateCurrentAngle(angle)
                    })
            }
        }
    }
}