package com.guicarneirodev.goniometro

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.guicarneirodev.goniometro.ui.theme.GoniometroTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.lang.Math.toDegrees
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            val validViewModel: ValidViewModel = viewModel()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { Home(navController) }
                composable("login") { Login(navController) }
                composable("register") { Register(navController, validViewModel) }
                composable("main") { Main() }
            }
        }
    }
}

@Composable
fun Main() {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: "defaultUser"

    GoniometroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Tutorial(userId = userId)
            Background()
            MainPhotoDisplay()
            Goniometro()
        }
    }
}

@Composable
fun Tutorial(userId: String) {
    val showTutorial = remember { mutableStateOf(true) }

    LaunchedEffect(key1 = userId) {
        val docRef = FirebaseFirestore.getInstance().collection("users").document(userId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Tutorial", "Listen failed.", e)
                return@addSnapshotListener
            }
            Log.d("Tutorial", "Snapshot: ${snapshot?.data}")
            Log.d("Auth", "UID do usuário autenticado: ${FirebaseAuth.getInstance().currentUser?.uid}")
            showTutorial.value = snapshot?.getBoolean("showTutorial") ?: true
        }

    }

    if (showTutorial.value) {
        Dialog(
            onDismissRequest = {
                showTutorial.value = false
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                IconButton(onClick = { showTutorial.value = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Fechar")
                                }
                            }
                            Text("Recomendações.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "1. Saiba os princípios básicos da goniometria.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "2. Selecione uma foto da galeria (canto inferior esquerdo) ou tire uma foto da articulação que será avaliada (canto inferior direito).")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "3. Clique em 'Realizar Goniometria' assim que tiver a foto posicionada no meio da tela")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "4. Arraste do Eixo até a posição correta do Braço Fixo com o dedo, então de um toque onde deverá ser posicionado o Braço Móvel.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "5. Caso não tenha realizado corretamente a goniometria, clique em 'Reiniciar Goniometria' para tentar novamente.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(onClick = {
                                showTutorial.value = false
                                FirebaseFirestore.getInstance().collection("users").document(userId)
                                    .update("showTutorial", false)
                            }) {
                                Text("Não exibir novamente")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Background() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF006F6A),
                        Color(0xFF006F6A)
                    )
                )
            )
    )
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MainPhotoDisplay() {
    val context = LocalContext.current
    var currentImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val importLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        currentImageUri = uri
    }

    val file = context.createImageFile()
    val captureUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val captureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) currentImageUri = captureUri
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            captureLauncher.launch(captureUri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
        IconButton(onClick = {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }, Modifier.padding(9.dp)) {
            Icon(painter = painterResource(id = R.drawable.photo_library),
                contentDescription = "Tirar Foto",
                modifier = Modifier
                    .size(44.dp)
                    .padding(1.dp))
        }
        IconButton(onClick = {
            importLauncher.launch("image/*")
        }, Modifier.padding(9.dp)) {
            Icon(painter = painterResource(id = R.drawable.photo_library),
                contentDescription = "Importar Foto",
                modifier = Modifier
                    .size(44.dp)
                    .padding(1.dp))
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd,
    ) {
        IconButton(
            onClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    captureLauncher.launch(captureUri)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }, Modifier.padding(9.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.addphoto),
                contentDescription = "Tirar Foto",
                modifier = Modifier
                    .size(44.dp)
                    .padding(1.dp)
            )
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        currentImageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Foto selecionada",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownMenuItem(onClick: () -> Unit, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onClick()
                    }
                }
                true
            }
    ) {
        content()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Goniometro() {
    var lineStart by remember { mutableStateOf(Offset.Zero) }
    var lineEnd by remember { mutableStateOf(Offset.Zero) }
    var lines by remember { mutableStateOf(listOf<Pair<Offset, Offset>>()) }
    var isLineSet by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedAngleIndex by remember { mutableIntStateOf(0) }
    val angleOptions = listOf("Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto")

    fun calculateAngleBetweenLines(
        start1: Offset,
        end1: Offset,
        start2: Offset,
        end2: Offset
    ): Double {
        val directionX1 = end1.x - start1.x
        val directionY1 = end1.y - start1.y
        val directionX2 = end2.x - start2.x
        val directionY2 = end2.y - start2.y

        val angleRadians1 = atan2(directionY1.toDouble(), directionX1.toDouble())
        val angleRadians2 = atan2(directionY2.toDouble(), directionX2.toDouble())

        val angleDifference = (toDegrees(angleRadians2 - angleRadians1) + 360) % 360
        val directAngle = if (angleDifference > 180) 360 - angleDifference else angleDifference

        return 180 - directAngle
    }

    fun calculateAllAngles(
        start1: Offset,
        end1: Offset,
        start2: Offset,
        end2: Offset
    ): List<Double> {
        val directAngle = calculateAngleBetweenLines(start1, end1, start2, end2)
        val oppositeAngle = 180 - directAngle
        val supplementaryAngle = (directAngle + 90) % 180
        val oppositeSupplementaryAngle = 180 - supplementaryAngle

        return listOf(directAngle, oppositeAngle, supplementaryAngle, oppositeSupplementaryAngle)
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
            if (isLineSet) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                        .pointerInteropFilter { motionEvent ->
                            when (motionEvent.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    if (lines.isEmpty()) {
                                        lineStart = Offset(motionEvent.x, motionEvent.y)
                                        lineEnd = lineStart
                                    } else {
                                        lineStart = lines[0].first
                                        lineEnd = Offset(motionEvent.x, motionEvent.y)
                                    }
                                }

                                MotionEvent.ACTION_MOVE -> {
                                    lineEnd = Offset(motionEvent.x, motionEvent.y)
                                }

                                MotionEvent.ACTION_UP -> {
                                    if (lines.size < 2) {
                                        lines = lines + (lineStart to lineEnd)
                                    }
                                }
                            }
                            true
                        }
                ) {
                    lines.forEach { (start, end) ->
                        drawLine(
                            color = Color.Black,
                            start = start,
                            end = end,
                            strokeWidth = 44f
                        )
                    }
                    if (lines.size == 2) {
                        val angle = calculateAllAngles(
                            lines[0].first,
                            lines[0].second,
                            lines[1].first,
                            lines[1].second
                        )
                        val selectedAngle = angle[selectedAngleIndex]
                        val formattedAngle = String.format("%.1f", selectedAngle)
                        val text = "  $formattedAngle° "
                        val textOffset = Offset(
                            (lines[1].first.x + lines[1].second.x) / 2,
                            (lines[1].first.y + lines[1].second.y) / 2
                        )

                        val paint = Paint().asFrameworkPaint()
                        val textSize = 40.dp.toPx()
                        paint.textSize = textSize
                        size.width * 0.8f
                        val textWidth = paint.measureText(text)
                        val textHeight = -paint.ascent() + paint.descent()
                        val textBounds = RectF(
                            textOffset.x - textWidth / 2,
                            textOffset.y - textHeight / 2,
                            textOffset.x + textWidth / 2,
                            textOffset.y + textHeight / 2
                        )

                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(textBounds.left, textBounds.top),
                            size = Size(textBounds.width(), textBounds.height()),
                            cornerRadius = CornerRadius(4f, 4f)
                        )

                        drawRoundRect(
                            color = Color.Black,
                            topLeft = Offset(textBounds.left, textBounds.top),
                            size = Size(textBounds.width(), textBounds.height()),
                            cornerRadius = CornerRadius(4f, 4f),
                            style = Stroke(10f)
                        )

                        drawIntoCanvas { canvas ->
                            paint.color = Color.Black.toArgb()
                            canvas.nativeCanvas.drawText(
                                text,
                                textOffset.x - textWidth / 2,
                                textOffset.y + textHeight / 2 - paint.descent(),
                                paint
                            )
                        }
                    }
                }
            }
            Button(
                onClick = {
                    if (isLineSet) {
                        lineStart = Offset.Zero
                        lineEnd = Offset.Zero
                        lines = emptyList()
                    }
                    isLineSet = !isLineSet
                },
                modifier = Modifier.padding(6.dp)
            ) {
                Text(
                    text = if (isLineSet) "Reiniciar Goniometria" else "Realizar Goniometria",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                )
            )
        }
    }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(6.dp), contentAlignment = Alignment.BottomCenter) {
        Button(onClick = { expanded = true }) {
            Text("Selecionar Ângulo")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            angleOptions.forEachIndexed { index, title ->
                DropdownMenuItem(onClick = {
                    selectedAngleIndex = index
                    expanded = false
                })
                {
                    Text(title)
                }
            }
        }
    }
}