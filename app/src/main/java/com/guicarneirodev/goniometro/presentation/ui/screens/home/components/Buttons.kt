package com.guicarneirodev.goniometro.presentation.ui.screens.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationButtons(navController: NavController) {
    // Estado para controlar o debounce
    var isClickable by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botão de Login com debounce
        Button(
            onClick = {
                if (isClickable) {
                    isClickable = false
                    navController.navigate("login")
                    // Reabilita o botão após 1 segundo
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        isClickable = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f),
                disabledContainerColor = Color.White.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
            enabled = isClickable
        ) {
            Text(
                text = "Fazer Login",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Botão de Registro com debounce
        Button(
            onClick = {
                if (isClickable) {
                    isClickable = false
                    navController.navigate("register")
                    // Reabilita o botão após 1 segundo
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        isClickable = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                disabledContainerColor = Color.White.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = isClickable
        ) {
            Text(
                text = "Criar Conta",
                color = Color(0xFF1E88E5),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}