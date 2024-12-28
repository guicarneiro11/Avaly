package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier, viewModel: GoniometroScreenViewModel
) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedMeasurementButton(isLineSet = viewModel.isLineSet.value, onClick = {
            if (viewModel.isLineSet.value) {
                viewModel.clearLines()
            }
            viewModel.toggleLineSet()
        })

        QuadrantSelector(
            selectedQuadrant = viewModel.selectedAngleIndex.value,
            onQuadrantSelected = viewModel::setSelectedAngleIndex
        )
    }
}

@Composable
fun AnimatedMeasurementButton(
    isLineSet: Boolean,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val backgroundColor by animateColorAsState(
        targetValue = if (isLineSet) Color(0xFFE57373) else Color(0xFF1E88E5),
        label = "buttonColor"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isLandscape) 48.dp else 56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = if (isLineSet)
                stringResource(R.string.restart_goniometry)
            else
                stringResource(R.string.start_goniometry),
            style = if (isLandscape)
                MaterialTheme.typography.bodyLarge
            else
                MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun QuadrantSelector(
    selectedQuadrant: Int, onQuadrantSelected: (Int) -> Unit
) {
    val quadrants = listOf(
        stringResource(R.string.direct_angle),
        stringResource(R.string.opposite_angle),
        stringResource(R.string.supplementary_angle),
        stringResource(R.string.opposite_supplementary)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.quadrant),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        quadrants.forEachIndexed { index, title ->
            QuadrantOption(title = title,
                isSelected = index == selectedQuadrant,
                onClick = { onQuadrantSelected(index) })
        }
    }
}

@Composable
fun QuadrantOption(
    title: String, isSelected: Boolean, onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFF1E88E5) else Color.White
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) Color.White else Color(0xFF1E88E5)
        )
    }
}