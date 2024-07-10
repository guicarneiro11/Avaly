package com.guicarneirodev.goniometro

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.RectF
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.guicarneirodev.goniometro.ui.theme.GoniometroTheme
import com.google.firebase.auth.FirebaseAuth
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.File
import java.lang.Math.toDegrees
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.atan2

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val validViewModel: ValidViewModel = viewModel()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") { Home(navController) }
                composable("login") { Login(navController) }
                composable("register") { Register(navController, validViewModel) }
                composable("main") { Main(navController) }
                composable("patients/{userId}", arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")
                        ?: throw IllegalStateException("UserID não encontrado na backStackEntry.")
                    Patients(navController = navController, userId)
                }
                composable("results/{userId}/{patientId}", arguments = listOf(
                    navArgument("userId") { type = NavType.StringType },
                    navArgument("patientId") { type = NavType.StringType }
                )) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getString("userId")
                        ?: throw IllegalStateException("UserID não encontrado na backStackEntry.")
                    val patientId = backStackEntry.arguments?.getString("patientId")
                        ?: throw IllegalStateException("PatientID não encontrado na backStackEntry.")
                    Results(navController = navController, userId, patientId)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: throw IllegalStateException("Usuário não está logado.")

    GoniometroTheme {
        Scaffold(
            topBar = { Goniometro(navController, userId) }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Background()
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
                        Color(0xFF2B6EA8),
                        Color(0xFF2B6EA8)
                    )
                )
            )
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun InstructionsList() {
    val instructions = listOf(
        "Dica 1: Escolha entre \"Importar Foto\" ou \"Tirar Foto\".",
        "Dica 2: Lembre-se de escolher um foto que esteja de frente para a articulação a ser medida, para evitar possíveis erros e aumentar a precisão da goniometria",
        "Dica 3: Clique em \"Realizar Goniometria\".",
        "Dica 4: Posicione seu dedo onde seria localizado o \"Eixo\" da articulação e, em seguida, arraste-o em direção à referência do \"Braço fixo do goniômetro\".",
        "Dica 5: Após isso, clique na posição de referência do \"Braço móvel do goniômetro\".",
        "Dica 6: Com isso você terá o ângulo formado entre os dois braços do Goniômetro.",
        "Dica 7: Você pode alterar a referência do ângulo em \"Alterar Quadrante\", dependendo do plano e articulação que estão sendo mensurados.",
        "Dica 8: Caso a goniometria não tenha sido feita corretamente, você pode clicar em \"Reiniciar Goniometria\" para tentar novamente.",
        "Dica 9: Você pode salvar os seus resultados na opção \"Goniometrias\" (Esta ferramenta ainda está em desenvolvimento e haverá melhorias no futuro).",
        "Dica 10: O goniômetro do aplicativo utiliza como referência um goniometro real, tanto em seus braços, eixo e seus quadrantes.",
        "Dica 11: Recomenda-se usar como referência o \"Livro: Fundamentos das Técnicas de Avaliação Musculoesquelética por Marcia E. Epler, M. Lynn Palmer\" em qualquer goniometria realizada, tanto no aplicativo quanto em um goniômetro físico.",
        "Observação: O aplicativo não tem como objetivo substituir o goniômetro físico, apenas servir como uma ferramenta alternativa para os fisioterapeutas. Portanto, ele pode apresentar imprecisões nesta etapa inicial de desenvolvimento, e toda crítica será bem-vinda para melhorar o seu funcionamento."
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(instructions) { instruction ->
            Text(text = instruction,
                modifier = Modifier.padding(8.dp),
                color = Color(0xFF000000),
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Default)
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
fun DropdownMenuItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
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
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            content()
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
fun Goniometro(navController: NavController, userId: String) {
    var lineStart by remember { mutableStateOf(Offset.Zero) }
    var lineEnd by remember { mutableStateOf(Offset.Zero) }
    var lines by remember { mutableStateOf(listOf<Pair<Offset, Offset>>()) }
    var isLineSet by remember { mutableStateOf(false) }
    var selectedAngleIndex by remember { mutableIntStateOf(0) }
    val angleOptions = listOf("Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto")
    var angleDropdownExpanded by remember { mutableStateOf(false) }
    var menuDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var currentImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var dialogOpen by remember { mutableStateOf(false) }
    val importLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            currentImageUri = uri
        }

    val file = context.createImageFile()
    val captureUri =
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    val captureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                currentImageUri = captureUri
                menuDropdownExpanded = false
            }
        }
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                captureLauncher.launch(captureUri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

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
    Box(modifier = Modifier.fillMaxSize()) {
        currentImageUri?.let {
            Image(
                painter = rememberImagePainter(it),
                contentDescription = "Foto selecionada",
                modifier = Modifier.fillMaxSize()
            )
        }
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
        TopAppBar(
            title = { Text("") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE6E6E6)),
            actions = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    IconButton(onClick = { menuDropdownExpanded = true }, Modifier.padding(1.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.menu),
                            contentDescription = "Menu",
                            modifier = Modifier.size(44.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = menuDropdownExpanded,
                        onDismissRequest = { menuDropdownExpanded = false },
                        modifier = Modifier
                            .padding(1.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(onClick = { angleDropdownExpanded = true }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.tibia),
                                    contentDescription = "Alterar Quadrante",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Alterar Quadrante",
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                                DropdownMenu(
                                    expanded = angleDropdownExpanded,
                                    onDismissRequest = { angleDropdownExpanded = false }
                                ) {
                                    angleOptions.forEachIndexed { index, title ->
                                        DropdownMenuItem(onClick = {
                                            selectedAngleIndex = index
                                            angleDropdownExpanded = false
                                        })
                                        {
                                            Text(title)
                                        }
                                    }
                                }
                                DropdownMenu(
                                    expanded = angleDropdownExpanded,
                                    onDismissRequest = { angleDropdownExpanded = false }
                                ) {
                                    angleOptions.forEachIndexed { index, title ->
                                        DropdownMenuItem(onClick = {
                                            selectedAngleIndex = index
                                            angleDropdownExpanded = false
                                        })
                                        {
                                            Text(title)
                                        }
                                    }
                                }
                            }
                            DropdownMenu(
                                expanded = angleDropdownExpanded,
                                onDismissRequest = { angleDropdownExpanded = false }
                            ) {
                                angleOptions.forEachIndexed { index, title ->
                                    DropdownMenuItem(onClick = {
                                        selectedAngleIndex = index
                                        angleDropdownExpanded = false
                                    })
                                    {
                                        Text(title)
                                    }
                                }
                            }
                        }
                        DropdownMenuItem(onClick = {
                            navController.navigate("patients/$userId")
                            menuDropdownExpanded = false
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.clinical_notes),
                                    contentDescription = "Pacients List",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Pacientes",
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                            }
                        }
                        DropdownMenuItem(onClick = {
                            importLauncher.launch("image/*")
                            menuDropdownExpanded = false
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.photo_library),
                                    contentDescription = "Importar Foto",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Importar Foto",
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                            }
                        }
                        DropdownMenuItem(onClick = {
                            val permissionCheckResult =
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                )
                            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                captureLauncher.launch(captureUri)
                            } else {
                                permissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.addphoto),
                                    contentDescription = "Tirar Foto",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Tirar Foto",
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                            }
                        }
                        DropdownMenuItem(onClick = {
                            dialogOpen = true
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.help),
                                    contentDescription = "Ajuda",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ajuda",
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                            }
                        }
                        if (dialogOpen) {
                            AlertDialog(onDismissRequest = { dialogOpen = false },
                                title = { Text(text = "Instruções",
                                    color = Color(0xFF000000),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif) },
                                text = {
                                    InstructionsList()
                                },
                                confirmButton = {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Button(
                                            onClick = {
                                                dialogOpen = false
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor =
                                                Color(0xFF266399)
                                            )
                                        ) {
                                            Text(text = "Fechar")
                                        }
                                    }
                                }
                            )
                        }
                        DropdownMenuItem(onClick = {
                            navController.popBackStack()
                            menuDropdownExpanded = false
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logout),
                                    contentDescription = "Logout",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Logout",
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                            }
                        }
                    }
                }
            }
        )
        Box(
            Modifier
                .fillMaxWidth()
                .padding(6.dp), contentAlignment = Alignment.TopCenter
        ) {
            Button(
                onClick = {
                    if (isLineSet) {
                        lineStart = Offset.Zero
                        lineEnd = Offset.Zero
                        lines = emptyList()
                    }
                    isLineSet = !isLineSet
                },
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
}