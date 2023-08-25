package com.example.goniometro

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.goniometro.ui.theme.GoniometroTheme

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoniometroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Rodape()
                    Background()
                    CameraPhoto()
                    Goniometro()
                }
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

    var capturedImageUri by remember {
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
        Modifier
            .fillMaxSize()
            .padding(5.dp)
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
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.addphoto),
                contentDescription = null,
            )
        }
    }

    Box{
        Background()
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
        if (angle < 0) angle += 180.0
        return angle
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        BotaoGoniometro(
            onClick = {
                if (isLineSet) {
                    lineStart = Offset.Zero
                    lineEnd = Offset.Zero
                    lines = emptyList()
                }
                isLineSet = !isLineSet
            },
            isLineSet = isLineSet)
        }
    }
        if (isLineSet) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
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
                    val angle = calculateAngle(lines[0].first, lines[0].second, lines[1].first, lines[1].second)
                    val formattedAngle = String.format("%.1f", angle)
                    val text = "    Ângulo: $formattedAngle°"
                    val textOffset = Offset(
                        (lines[1].first.x + lines[1].second.x) / 2,
                        (lines[1].first.y + lines[1].second.y) / 2
                    )

                    LocalDensity
                    val textSize = 30.dp.toPx()

                    drawCircle(
                        color = Color.Blue,
                        radius = 30f,
                        center = textOffset
                    )
                    drawIntoCanvas { canvas ->
                        val paint = Paint().asFrameworkPaint()
                        paint.textSize = textSize
                        paint.color = Color.Black.toArgb()

                        canvas.nativeCanvas.drawText(
                            text,
                            textOffset.x,
                            textOffset.y + paint.textSize / 2,
                            paint
                        )
                    }
                }
            }
        }
    }

@Composable
fun BotaoGoniometro(onClick: () -> Unit, isLineSet: Boolean) {
    Button(
        onClick = onClick,
        Modifier.padding(72.dp),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(if (isLineSet) "Reiniciar Goniometria" else "Realizar Goniometria",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun Background(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.gradient),
        contentDescription = "Gradient",
        modifier = modifier
            .fillMaxSize()
            .size(400.dp, 121.dp)
    )
}

@Composable
fun Rodape(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.rectangle7),
        contentDescription = "Rectangle",
        modifier = modifier
            .width(width = 400.dp)
            .height(height = 61.dp)
            .background(color = Color(0xff33bfb7)))
}