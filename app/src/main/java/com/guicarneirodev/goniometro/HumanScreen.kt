package com.guicarneirodev.goniometro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

class HumanComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HumanComposeScreen()
        }
    }
}

@Composable
fun HumanComposeScreen() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val intent = Intent(context, ModelSelectionActivity::class.java)
        context.startActivity(intent)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Carregando a seleção de modelos...")
    }
}
