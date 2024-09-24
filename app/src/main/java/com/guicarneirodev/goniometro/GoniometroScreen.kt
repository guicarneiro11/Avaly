package com.guicarneirodev.goniometro

import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun GoniometroScreen(navController: NavController, userId: String, viewModel: GoniometroViewModel = viewModel()) {
    val lineStart by viewModel.lineStart
    val lineEnd by viewModel.lineEnd
    val lines by viewModel.lines
    val isLineSet by viewModel.isLineSet
    val selectedAngleIndex by viewModel.selectedAngleIndex
    val currentImageUri by viewModel.currentImageUri

    val angleOptions = listOf("Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto")
    var angleDropdownExpanded by remember { mutableStateOf(false) }
    var menuDropdownExpanded by remember { mutableStateOf(false) }
    var dialogOpen by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val importLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        viewModel.setCurrentImageUri(uri)
    }

    val file = context.createImageFile()
    val captureUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val captureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.setCurrentImageUri(captureUri)
            menuDropdownExpanded = false
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            captureLauncher.launch(captureUri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    GoniometroContent(
        lineStart = lineStart,
        lineEnd = lineEnd,
        lines = lines,
        isLineSet = isLineSet,
        selectedAngleIndex = selectedAngleIndex,
        currentImageUri = currentImageUri,
        angleOptions = angleOptions,
        angleDropdownExpanded = angleDropdownExpanded,
        menuDropdownExpanded = menuDropdownExpanded,
        dialogOpen = dialogOpen,
        onLineStartChange = viewModel::setLineStart,
        onLineEndChange = viewModel::setLineEnd,
        onAddLine = viewModel::addLine,
        onClearLines = viewModel::clearLines,
        onToggleLineSet = viewModel::toggleLineSet,
        onSelectedAngleIndexChange = viewModel::setSelectedAngleIndex,
        onAngleDropdownExpandedChange = { angleDropdownExpanded = it },
        onMenuDropdownExpandedChange = { menuDropdownExpanded = it },
        onDialogOpenChange = { dialogOpen = it },
        onImportImage = { importLauncher.launch("image/*") },
        onCaptureImage = {
            val permissionCheckResult = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                captureLauncher.launch(captureUri)
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        },
        navController = navController,
        userId = userId
    )
}