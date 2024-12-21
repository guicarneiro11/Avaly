package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.guicarneirodev.goniometro.presentation.ui.components.GoniometroCanvas
import com.guicarneirodev.goniometro.presentation.ui.components.InstructionsDialog

@Composable
fun GoniometroContent(
    lineStart: Offset,
    lineEnd: Offset,
    lines: List<Pair<Offset, Offset>>,
    isLineSet: Boolean,
    selectedAngleIndex: Int,
    currentImageUri: Uri?,
    angleOptions: List<String>,
    angleDropdownExpanded: Boolean,
    menuDropdownExpanded: Boolean,
    dialogOpen: Boolean,
    onLineStartChange: (Offset) -> Unit,
    onLineEndChange: (Offset) -> Unit,
    onAddLine: (Pair<Offset, Offset>) -> Unit,
    onClearLines: () -> Unit,
    onToggleLineSet: () -> Unit,
    onSelectedAngleIndexChange: (Int) -> Unit,
    onAngleDropdownExpandedChange: (Boolean) -> Unit,
    onMenuDropdownExpandedChange: (Boolean) -> Unit,
    onDialogOpenChange: (Boolean) -> Unit,
    onImportImage: () -> Unit,
    onCaptureImage: () -> Unit,
    navController: NavController,
    userId: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage(currentImageUri)

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLineSet) {
                GoniometroCanvas(
                    lineStart = lineStart,
                    lineEnd = lineEnd,
                    lines = lines,
                    selectedAngleIndex = selectedAngleIndex,
                    onLineStartChange = onLineStartChange,
                    onLineEndChange = onLineEndChange,
                    onAddLine = onAddLine
                )
            }

            TopBar(
                angleOptions = angleOptions,
                angleDropdownExpanded = angleDropdownExpanded,
                menuDropdownExpanded = menuDropdownExpanded,
                onAngleDropdownExpandedChange = onAngleDropdownExpandedChange,
                onMenuDropdownExpandedChange = onMenuDropdownExpandedChange,
                onSelectedAngleIndexChange = onSelectedAngleIndexChange,
                onImportImage = onImportImage,
                onCaptureImage = onCaptureImage,
                onDialogOpenChange = onDialogOpenChange,
                navController = navController,
                userId = userId
            )

            GoniometroButton(
                isLineSet = isLineSet,
                onToggleLineSet = {
                    if (isLineSet) {
                        onClearLines()
                    }
                    onToggleLineSet()
                }
            )
        }
    }

    if (dialogOpen) {
        InstructionsDialog(onDismiss = { onDialogOpenChange(false) })
    }
}

@Composable
fun GoniometroButton(
    isLineSet: Boolean,
    onToggleLineSet: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(6.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Button(
            onClick = onToggleLineSet,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF266399)),
            modifier = Modifier.padding(2.dp)
        ) {
            Text(
                text = if (isLineSet) "Reiniciar Goniometria" else "Realizar Goniometria",
                color = Color(0xFFFFFFFF),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun BackgroundImage(currentImageUri: Uri?) {
    currentImageUri?.let {
        Image(
            painter = rememberAsyncImagePainter(it),
            contentDescription = "Foto selecionada",
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF2B6EA8), Color(0xFF2B6EA8))
                    )
                )
        )
    }
}