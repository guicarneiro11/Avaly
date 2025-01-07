package com.guicarneirodev.goniometro.presentation.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun WelcomeText() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_to),
            color = PrimaryLight,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = stringResource(R.string.app_name_slogan),
            color = PrimaryLight,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.app_description),
            color = PrimaryLight.copy(alpha = 0.8f),
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        )
    }
}