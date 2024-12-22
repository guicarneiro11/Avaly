package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
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
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

    DisposableEffect(configuration) {
        onDispose {
            orientation = configuration.orientation
        }
    }

    Scaffold(
        topBar = {
            ModernTopBar(
                viewModel = viewModel,
                navController = navController
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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

            when (configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> LandscapeLayout(viewModel)
                else -> PortraitLayout(viewModel)
            }
        }
    }
}

@Composable
private fun PortraitLayout(viewModel: GoniometroScreenViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        ImageWithGoniometer(viewModel)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Área para o botão "Realizar Goniometria"

            Spacer(modifier = Modifier.weight(1f))

            // Painel de controle na parte inferior
            ControlPanel(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.95f),
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(16.dp),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ControlPanel(
    modifier: Modifier = Modifier,
    viewModel: GoniometroScreenViewModel
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botão principal com animação
        AnimatedMeasurementButton(
            isLineSet = viewModel.isLineSet.value,
            onClick = {
                if (viewModel.isLineSet.value) {
                    viewModel.clearLines()
                }
                viewModel.toggleLineSet()
            }
        )

        // Seletor de quadrante
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
    navController: NavController
) {
    val angleOptions = listOf("Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto")
    var angleDropdownExpanded by remember { mutableStateOf(false) }
    var menuDropdownExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(alpha = 0.95f),
            actionIconContentColor = Color(0xFF1E88E5)
        ),
        actions = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Menu Principal
                    IconButton(
                        onClick = { menuDropdownExpanded = true },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF1E88E5)
                        )
                    }

                    // Dropdown Menu Moderno
                    DropdownMenu(
                        expanded = menuDropdownExpanded,
                        onDismissRequest = { menuDropdownExpanded = false },
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .width(280.dp)
                    ) {
                        ModernMenuItems(
                            viewModel = viewModel,
                            navController = navController,
                            onDismiss = { menuDropdownExpanded = false }
                        )
                    }

                    AngleDropdownMenu(
                        expanded = angleDropdownExpanded,
                        onDismissRequest = { angleDropdownExpanded = false },
                        angleOptions = angleOptions,
                        onSelectedAngleIndexChange = viewModel::setSelectedAngleIndex
                    )
                }
            }
        }
    )
}

@Composable
private fun ModernMenuItems(
    viewModel: GoniometroScreenViewModel,
    navController: NavController,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: ""

    // Launcher para importar imagem
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.setCurrentImageUri(uri)
    }

    // Launcher para captura de foto
    val file = context.createImageFile()
    val captureUri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    )

    val captureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.setCurrentImageUri(captureUri)
            onDismiss()
        }
    }

    // Permission launcher para câmera
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            captureLauncher.launch(captureUri)
        } else {
            Toast.makeText(context, "Permissão negada", Toast.LENGTH_SHORT).show()
        }
    }

    ModernMenuItem(
        icon = painterResource(id = R.drawable.clinical_notes),
        text = "Pacientes",
        onClick = {
            navController.navigate("patients/$userId")
            onDismiss()
        }
    )

    ModernMenuItem(
        icon = painterResource(id = R.drawable.photo_library),
        text = "Importar Foto",
        onClick = {
            importLauncher.launch("image/*")
            onDismiss()
        }
    )

    ModernMenuItem(
        icon = painterResource(id = R.drawable.addphoto),
        text = "Capturar Foto",
        onClick = {
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

    ModernMenuItem(
        icon = painterResource(id = R.drawable.help),
        text = "Ajuda",
        onClick = {
            // Implementar ajuda
            onDismiss()
        }
    )

    ModernMenuItem(
        icon = painterResource(id = R.drawable.logout),
        text = "Logout",
        onClick = {
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
            onDismiss()
        }
    )
}

@Composable
private fun ModernMenuItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit
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
            modifier = Modifier
                .padding(12.dp),
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
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start
    ) {
        // Área da imagem com goniômetro (ocupando maior parte da tela)
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxHeight()
        ) {
            ImageWithGoniometer(viewModel)
        }

        // Painel de controle lateral
        ControlPanel(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxHeight()
                .background(
                    color = Color.White.copy(alpha = 0.95f)
                )
                .padding(16.dp),
            viewModel = viewModel
        )
    }
}

@Composable
fun ImageWithGoniometer(viewModel: GoniometroScreenViewModel) {
    val currentImageUri by viewModel.currentImageUri
    val isLineSet by viewModel.isLineSet

    Box(modifier = Modifier.fillMaxSize()) {
        // Background com a imagem
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

        // Box para o GoniometroCanvas
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isLineSet) {
                GoniometroCanvas(
                    lineStart = viewModel.lineStart.value,
                    lineEnd = viewModel.lineEnd.value,
                    lines = viewModel.lines.value,
                    selectedAngleIndex = viewModel.selectedAngleIndex.value,
                    onLineStartChange = viewModel::setLineStart,
                    onLineEndChange = viewModel::setLineEnd,
                    onAddLine = viewModel::addLine
                )
            }
        }
    }
}

@Composable
fun AnimatedMeasurementButton(
    isLineSet: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isLineSet) Color(0xFFE57373) else Color(0xFF1E88E5),
        label = "buttonColor"
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
    selectedQuadrant: Int,
    onQuadrantSelected: (Int) -> Unit
) {
    val quadrants = listOf(
        "Ângulo Direto",
        "Ângulo Oposto",
        "Ângulo Suplementar",
        "Suplementar Oposto"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
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
            QuadrantOption(
                title = title,
                isSelected = index == selectedQuadrant,
                onClick = { onQuadrantSelected(index) }
            )
        }
    }
}

@Composable
fun QuadrantOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
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
