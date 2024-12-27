package com.guicarneirodev.goniometro.presentation.ui.screens.results.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsAppBar(
    navController: NavController,
    onAddAngle: (String, String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Resultados", color = Color(0xFF1E88E5)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color(0xFF1E88E5)
                )
            }
        },
        actions = {
            IconButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Adicionar",
                    tint = Color(0xFF1E88E5)
                )
            }
        }
    )

    if (showAddDialog) {
        ModernAddDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, value ->
                onAddAngle(name, value)
                showAddDialog = false
            }
        )
    }
}