package com.guicarneirodev.goniometro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("destination", "modelSelectionLoading")
        }
        context.startActivity(intent)
    }
}
