package com.guicarneirodev.goniometro

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.graphics.asImageBitmap
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.guicarneirodev.goniometro.ui.theme.GoniometroTheme
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin{
            androidContext(this@MainActivity)
            modules(viewModelModule)
        }

        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            val validViewModel: ValidViewModel = viewModel()

            NavHost(navController = navController, startDestination = "inicio") {
                composable("inicio") { Inicio(navController) }
                composable("login") { Login(navController) }
                composable("register") { Register(navController, validViewModel) }
                composable("home") { Home() }
            }
        }
    }
}

@Composable
fun Home() {
    GoniometroTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Tutorial()
            Background()
            PhotoImport()
            CameraPhoto()
            Goniometro()
        }
    }
}

@Composable
fun Tutorial() {
    var showTutorial by rememberSaveable { mutableStateOf(true) }

    if (showTutorial) {
        Dialog(
            onDismissRequest = {
                showTutorial = false
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
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Recomendações.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "1. Saiba os principals básicos da goniometria.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "2. Selecione uma foto da galeria (canto inferior esquerdo) ou tire uma foto da articulação que será avaliada (canto inferior direito).")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "3. Clique em 'Realizar Goniometria' assim que tiver a foto posicionada no meio da tela")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "4. Arraste do Eixo até a posição correta do Braço Fixo com o dedo, então de um toque onde deverá ser posicionado o Braço Móvel.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "5. Caso não tenha realizado corretamente a goniometria, clique em 'Reiniciar Goniometria' para tentar novamente.")
                            Spacer(modifier = Modifier.height(4.dp))
                            Button(onClick = { showTutorial = false }) {
                                Text("Fechar")
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

@Composable
fun PhotoImport() {
    var imageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  rememberSaveable {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomStart,
    ) {
        IconButton(onClick = {
            launcher.launch("image/*")
        }, Modifier.padding(9.dp))
        {
            Icon(painter = painterResource(id = R.drawable.photo_library), contentDescription = "Importar Foto",
                modifier = Modifier
                    .size(44.dp)
                    .padding(1.dp))
        }
        imageUri?.let {
            bitmap.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            } else {
                val inputStream = context.contentResolver.openInputStream(it)
                BitmapFactory.decodeStream(inputStream)
            }

            bitmap.value?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(1.dp)
                        .align(alignment = Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CameraPhoto() {
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by rememberSaveable {
        mutableStateOf<Uri?>(null)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
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
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }
            }, Modifier.padding(9.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.addphoto),
                contentDescription = null,
            )
        }
    }

    Box{
        if (capturedImageUri?.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .align(alignment = Alignment.Center),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
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
fun Goniometro() {
    var lineStart by remember { mutableStateOf(Offset.Zero) }
    var lineEnd by remember { mutableStateOf(Offset.Zero) }
    var lines by remember { mutableStateOf(listOf<Pair<Offset, Offset>>()) }
    var isLineSet by remember { mutableStateOf(false) }

    fun calculateAngle(start1: Offset, end1: Offset, start2: Offset, end2: Offset): Double {
        val dx1 = end1.x - start1.x
        val dy1 = end1.y - start1.y
        val dx2 = end2.x - start2.x
        val dy2 = end2.y - start2.y

        val angle1 = atan2(dy1.toDouble(), dx1.toDouble())
        val angle2 = atan2(dy2.toDouble(), dx2.toDouble())

        var angle = Math.toDegrees(angle2 - angle1)
        if (angle < 0) angle += 360
        return angle
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
                    val angle = calculateAngle(
                        lines[0].first,
                        lines[0].second,
                        lines[1].first,
                        lines[1].second
                    )
                    val formattedAngle = String.format("%.1f", angle)
                    val text = "  $formattedAngle° "
                    val textOffset = Offset(
                        (lines[1].first.x + lines[1].second.x) / 2,
                        (lines[1].first.y + lines[1].second.y) / 2
                    )

                    drawCircle(
                        color = Color.Black,
                        radius = 20f,
                        center = textOffset
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
            modifier = Modifier.padding(12.dp)
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
}

@Composable
fun Voltar(navController: NavController) {
    val lastClick = remember { mutableLongStateOf(0L) }

    Box(
        contentAlignment = Alignment.TopStart,
    ) {
        IconButton(
            onClick = {
                val now = System.currentTimeMillis()
                if (now - lastClick.longValue > 3000) {
                    navController.popBackStack()
                    lastClick.longValue = now
                }
            },
            Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrowback),
                contentDescription = "Voltar Tela",
                modifier = Modifier
                    .size(50.dp)
            )
        }
    }
}