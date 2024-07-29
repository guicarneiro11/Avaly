package com.guicarneirodev.goniometro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class ModelSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            initializeFirebaseRemoteConfig()

            NavHost(navController = navController, startDestination = "modelSelectionLoading") {
                composable("modelSelectionLoading") { ModelSelectionLoading(navController) }
                composable("modelSelection") { ModelSelectionScreenWrapper(navController) }
            }
        }
    }

    private fun initializeFirebaseRemoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            task.takeIf { it.isSuccessful }?.let {
                val developerKey = remoteConfig.getString("DEVELOPER_KEY")
                val developerSecret = remoteConfig.getString("DEVELOPER_SECRET")
                TokenManager.developerKey = developerKey
                TokenManager.developerSecret = developerSecret
            } ?: run {
                println("Falha ao buscar configuração")
            }
        }
    }
}

@Composable
fun ModelSelectionScreenWrapper(navController: NavController) {
    val accessTokenState = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val accessToken = TokenManager.getAccessToken()
        accessTokenState.value = accessToken
    }

    accessTokenState.value.takeIf { it != null }?.let { accessToken ->
        ModelSelectionScreen(accessToken, navController)
    } ?: ModelSelectionLoading(navController)
}

@Composable
fun ModelSelectionLoading(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            TokenManager.getAccessToken()
            navController.navigate("modelSelection")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectionScreen(accessToken: String, navController: NavController) {
    val context = LocalContext.current
    val models = remember { mutableStateOf<List<Model>>(emptyList()) }
    var searchModels by remember { mutableStateOf(false)}
    var searchTerm by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(accessToken, searchTerm) {
        val fetchedModels = fetchMyHumanModels(accessToken)

        models.value = searchTerm.takeIf { it.isNotEmpty() }
            ?.let { term ->
                fetchedModels.filter { it.content_title.contains(term, ignoreCase = true) }
            } ?: fetchedModels
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchTerm,
                        onValueChange = { newValue ->
                            searchTerm = newValue
                            searchModels = true
                        },
                        placeholder = { Text("Pesquisar") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE6E6E6)),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("main") {
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.voltar),
                            contentDescription = "Voltar",
                            tint = Color(0xFF000000)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(models.value) { model ->
                ModelItem(model = model) {
                    val intent = Intent(context, HumanMain::class.java).apply {
                        putExtra(HumanMain.MODEL_MESSAGE, model.content_id)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun ModelItem(model: Model, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = model.content_title)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                model = model.content_thumbnail_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}