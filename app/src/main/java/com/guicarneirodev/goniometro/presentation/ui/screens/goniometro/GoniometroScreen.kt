package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.guicarneirodev.goniometro.BuildConfig
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.ui.components.GoniometroCanvas
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.BackgroundDecorations
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

    Scaffold(topBar = {
        ModernTopBar(viewModel = viewModel,
            navController = navController,
            onImportImage = { importLauncher.launch("image/*") },
            onCaptureImage = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    captureLauncher.launch(captureUri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E88E5), Color(0xFF4FC3F7)
                        )
                    )
                )
        ) {
            BackgroundDecorations()

            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> LandscapeLayout(viewModel)
                else -> PortraitLayout(viewModel)
            }
        }
    }
}

@Composable
private fun PortraitLayout(viewModel: GoniometroScreenViewModel) {
    val currentImageUri by viewModel.currentImageUri
    val isLineSet by viewModel.isLineSet

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
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
                        color = Color(0xFF1E88E5).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            BackgroundImage(
                currentImageUri = currentImageUri, modifier = Modifier.fillMaxSize()
            )

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

        ControlPanel(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.95f),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(16.dp), viewModel = viewModel
        )
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(
    viewModel: GoniometroScreenViewModel,
    navController: NavController,
    onImportImage: () -> Unit,
    onCaptureImage: () -> Unit
) {
    val angleOptions =
        listOf("Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto")
    var angleDropdownExpanded by remember { mutableStateOf(false) }
    var menuDropdownExpanded by remember { mutableStateOf(false) }
    val currentAngle by viewModel.currentAngle.collectAsState()

    TopAppBar(title = { }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White.copy(alpha = 0.95f),
        actionIconContentColor = Color(0xFF1E88E5)
    ), actions = {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Menu Button à esquerda
                Box(modifier = Modifier.size(48.dp)) {
                    IconButton(onClick = { menuDropdownExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF1E88E5)
                        )
                    }
                }

                // Angle Display fixo à direita
                Box(
                    modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd
                ) {
                    AngleDisplay(currentAngle)
                }
            }

            // DropdownMenu separado do Row principal
            DropdownMenu(
                expanded = menuDropdownExpanded,
                onDismissRequest = { menuDropdownExpanded = false },
                modifier = Modifier
                    .background(
                        color = Color.White, shape = RoundedCornerShape(12.dp)
                    )
                    .width(280.dp)
            ) {
                ModernMenuItems(navController = navController,
                    onDismiss = { menuDropdownExpanded = false },
                    onImportImage = {
                        onImportImage()
                        menuDropdownExpanded = false
                    },
                    onCaptureImage = {
                        onCaptureImage()
                        menuDropdownExpanded = false
                    })
            }

            if (angleDropdownExpanded) {
                AngleDropdownMenu(
                    expanded = angleDropdownExpanded,
                    onDismissRequest = { angleDropdownExpanded = false },
                    angleOptions = angleOptions,
                    onSelectedAngleIndexChange = viewModel::setSelectedAngleIndex
                )
            }
        }
    })
}

@Composable
fun AngleDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    angleOptions: List<String>,
    onSelectedAngleIndexChange: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded, onDismissRequest = onDismissRequest
    ) {
        angleOptions.forEachIndexed { index, title ->
            DropdownMenuItem(onClick = {
                onSelectedAngleIndexChange(index)
                onDismissRequest()
            }) {
                Text(title)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownMenuItem(
    onClick: () -> Unit, content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(Color.White, shape = RoundedCornerShape(2.dp))
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    onClick()
                }
            }
            true
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            content()
        }
    }
}

@Composable
fun AngleDisplay(angle: Double?) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = Color.White, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(text = "Ângulo encontrado: ${angle?.let { String.format("%.1f°", it) } ?: "--°"}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E88E5))
    }
}

@Composable
fun ModernMenuItems(
    navController: NavController,
    onDismiss: () -> Unit,
    onImportImage: () -> Unit,
    onCaptureImage: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: ""

    ModernMenuItem(icon = painterResource(id = R.drawable.clinical_notes),
        text = "Pacientes",
        onClick = {
            navController.navigate("patients/$userId")
            onDismiss()
        })

    ModernMenuItem(
        icon = painterResource(id = R.drawable.photo_library),
        text = "Importar Foto",
        onClick = onImportImage
    )

    ModernMenuItem(
        icon = painterResource(id = R.drawable.addphoto),
        text = "Capturar Foto",
        onClick = onCaptureImage
    )

    ModernMenuItem(icon = painterResource(id = R.drawable.help), text = "Ajuda", onClick = {
        // Implementar ajuda
        onDismiss()
    })

    ModernMenuItem(icon = painterResource(id = R.drawable.logout), text = "Logout", onClick = {
        navController.navigate("login") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
        onDismiss()
    })
}

@Composable
private fun ModernMenuItem(
    icon: Painter, text: String, onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF1E88E5)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LandscapeLayout(viewModel: GoniometroScreenViewModel) {
    Row(
        modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
        ) {
            ImageWithGoniometer(viewModel)
        }

        ControlPanel(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
                .background(
                    color = Color.White.copy(alpha = 0.95f)
                )
                .padding(16.dp), viewModel = viewModel
        )
    }
}

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

@Composable
fun BackgroundImage(currentImageUri: Uri?, modifier: Modifier = Modifier) {
    currentImageUri?.let {
        Box(modifier = modifier) {
            Image(
                painter = rememberAsyncImagePainter(model = it, onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Success -> Log.d(
                            "BackgroundImage",
                            "Imagem carregada com sucesso"
                        )

                        is AsyncImagePainter.State.Error -> Log.e(
                            "BackgroundImage",
                            "Erro ao carregar imagem: ${state.result.throwable}"
                        )

                        else -> {}
                    }
                }),
                contentDescription = "Foto selecionada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun AnimatedMeasurementButton(
    isLineSet: Boolean, onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isLineSet) Color(0xFFE57373) else Color(0xFF1E88E5), label = "buttonColor"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = if (isLineSet) "Reiniciar Goniometria" else "Realizar Goniometria",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun QuadrantSelector(
    selectedQuadrant: Int, onQuadrantSelected: (Int) -> Unit
) {
    val quadrants = listOf(
        "Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto"
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
            text = "Quadrante",
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