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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsAppBar(
    navController: NavController,
    onAddAngle: (String, String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                stringResource(R.string.results),
                color = AccentBlue
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryLight.copy(alpha = 0.95f)
        ),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = AccentBlue
                )
            }
        },
        actions = {
            IconButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = stringResource(R.string.add_action),
                    tint = AccentBlue
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