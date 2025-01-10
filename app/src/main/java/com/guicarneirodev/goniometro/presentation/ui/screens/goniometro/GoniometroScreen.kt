package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.BuildConfig
import com.guicarneirodev.goniometro.presentation.ui.reusable.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components.LandscapeLayout
import com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components.TopBar
import com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components.PortraitLayout
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import com.guicarneirodev.goniometro.utils.createImageFile

@Composable
fun GoniometroScreen(navController: NavController) {
    val viewModel: GoniometroScreenViewModel = viewModel()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Log.d("GoniometryScreen", "Imagem importada: $it")
                viewModel.setCurrentImageUri(it)
            }
        }

    val file = remember { context.createImageFile() }
    val captureUri = remember {
        FileProvider.getUriForFile(
            context, "${BuildConfig.APPLICATION_ID}.provider", file
        ).also { Log.d("GoniometryScreen", "URI de captura criado: $it") }
    }

    val captureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d("GoniometryScreen", "Foto capturada com sucesso: $captureUri")
                viewModel.setCurrentImageUri(captureUri)
            } else {
                Log.e("GoniometryScreen", "Falha ao capturar foto")
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            captureLauncher.launch(captureUri)
        } else {
            Toast.makeText(context, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
        }
    }

    var orientation by remember { mutableIntStateOf(configuration.orientation) }

    DisposableEffect(configuration) {
        orientation = configuration.orientation
        onDispose { }
    }

    Scaffold(
        topBar = {
            TopBar(
                viewModel = viewModel,
                navController = navController,
                onImportImage = { importLauncher.launch("image/*") },
                onCaptureImage = {
                    val permissionCheckResult = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    )
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        captureLauncher.launch(captureUri)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(SecondaryDark)
        ) {
            BackgroundDecorations()

            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> LandscapeLayout(viewModel)
                else -> PortraitLayout(viewModel)
            }
        }
    }
}