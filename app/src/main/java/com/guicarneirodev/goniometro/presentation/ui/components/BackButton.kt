package com.guicarneirodev.goniometro.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R

@Composable
fun BackButton(onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.voltar),
        contentDescription = "Voltar Tela",
        modifier = Modifier
            .clickable(onClick = onClick)
            .size(40.dp),
        tint = Color(0xFF000000)
    )
}