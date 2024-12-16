package com.guicarneirodev.goniometro.presentation.ui.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size

@Composable
fun LogoSection() {
    Box(
        modifier = Modifier
            .size(96.dp)
            .padding(8.dp)
    ) {
        // Quadrado base rotacionado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(45f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(16.dp)
                )
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .rotate(45f)
                .background(
                    color = Color(0xFF1E88E5),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "A",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.rotate(-45f)
            )
        }
    }
}