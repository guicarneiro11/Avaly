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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class ModelSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initializeFirebaseRemoteConfig()

            ModelSelectionScreenWrapper()
        }
    }
}

fun initializeFirebaseRemoteConfig() {
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }
    remoteConfig.setConfigSettingsAsync(configSettings)

    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val developerKey = remoteConfig.getString("DEVELOPER_KEY")
            val developerSecret = remoteConfig.getString("DEVELOPER_SECRET")
            TokenManager.developerKey = developerKey
            TokenManager.developerSecret = developerSecret
        } else {
            println("Falha ao buscar configuração")
        }
    }
}

@Composable
fun ModelSelectionScreenWrapper() {
    val accessTokenState = remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val accessToken = TokenManager.getAccessToken()
            accessTokenState.value = accessToken
        }
    }

    if (accessTokenState.value != null) {
        ModelSelectionScreen(accessToken = accessTokenState.value!!)
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Carregando a seleção de modelos...")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSelectionScreen(accessToken: String) {
    val context = LocalContext.current
    val models = remember { mutableStateOf<List<Model>>(emptyList()) }

    LaunchedEffect(accessToken) {
        val fetchedModels = fetchMyHumanModels(accessToken)
        models.value = fetchedModels
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Selecione um Modelo Ortopédico") })
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(models.value) { model ->
                ModelItem(model) {
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