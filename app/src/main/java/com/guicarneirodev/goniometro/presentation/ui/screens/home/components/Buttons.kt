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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationButtons(navController: NavController) {
    var isClickable by remember { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                if (isClickable) {
                    isClickable = false
                    navController.navigate("login")
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
                containerColor = PrimaryLight.copy(alpha = 0.1f),
                disabledContainerColor = PrimaryLight.copy(alpha = 0.05f)
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, PrimaryLight.copy(alpha = 0.2f)),
            enabled = isClickable
        ) {
            Text(
                text = stringResource(R.string.login),
                color = PrimaryLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Button(
            onClick = {
                if (isClickable) {
                    isClickable = false
                    navController.navigate("register")
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
                containerColor = AccentBlue,
                disabledContainerColor = AccentBlue.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = isClickable
        ) {
            Text(
                text = stringResource(R.string.create_account),
                color = PrimaryLight,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}