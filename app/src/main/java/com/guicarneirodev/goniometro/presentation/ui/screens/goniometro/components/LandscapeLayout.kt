package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun LandscapeLayout(viewModel: GoniometroScreenViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
        ) {
            ImageWithGoniometer(viewModel)
        }

        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
                .background(color = PrimaryLight.copy(alpha = 0.95f))
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AnimatedMeasurementButton(
                        isLineSet = viewModel.isLineSet.value,
                        onClick = {
                            if (viewModel.isLineSet.value) {
                                viewModel.clearLines()
                            }
                            viewModel.toggleLineSet()
                        }
                    )
                }

                item {
                    QuadrantSelector(
                        selectedQuadrant = viewModel.selectedAngleIndex.value,
                        onQuadrantSelected = viewModel::setSelectedAngleIndex
                    )
                }
            }
        }
    }
}