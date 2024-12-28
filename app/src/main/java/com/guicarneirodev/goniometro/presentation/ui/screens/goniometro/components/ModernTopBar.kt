package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.viewmodel.GoniometroScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernTopBar(
    viewModel: GoniometroScreenViewModel,
    navController: NavController,
    onImportImage: () -> Unit,
    onCaptureImage: () -> Unit
) {
    val angleOptions =
        listOf("Ângulo Direto", "Ângulo Oposto", "Ângulo Suplementar", "Suplementar Oposto")
    var angleDropdownExpanded by remember { mutableStateOf(false) }
    var menuDropdownExpanded by remember { mutableStateOf(false) }
    val currentAngle by viewModel.currentAngle.collectAsState()

    TopAppBar(title = { }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = Color.White.copy(alpha = 0.95f),
        actionIconContentColor = Color(0xFF1E88E5)
    ), actions = {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(modifier = Modifier.size(48.dp)) {
                    IconButton(onClick = { menuDropdownExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF1E88E5)
                        )
                    }
                }

                Box(
                    modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd
                ) {
                    AngleDisplay(currentAngle)
                }
            }

            DropdownMenu(
                expanded = menuDropdownExpanded,
                onDismissRequest = { menuDropdownExpanded = false },
                modifier = Modifier
                    .background(
                        color = Color.White, shape = RoundedCornerShape(12.dp)
                    )
                    .width(280.dp)
            ) {
                ModernMenuItems(navController = navController,
                    onDismiss = { menuDropdownExpanded = false },
                    onImportImage = {
                        onImportImage()
                        menuDropdownExpanded = false
                    },
                    onCaptureImage = {
                        onCaptureImage()
                        menuDropdownExpanded = false
                    })
            }

            if (angleDropdownExpanded) {
                AngleDropdownMenu(
                    expanded = angleDropdownExpanded,
                    onDismissRequest = { angleDropdownExpanded = false },
                    angleOptions = angleOptions,
                    onSelectedAngleIndexChange = viewModel::setSelectedAngleIndex
                )
            }
        }
    })
}

@Composable
fun AngleDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    angleOptions: List<String>,
    onSelectedAngleIndexChange: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded, onDismissRequest = onDismissRequest
    ) {
        angleOptions.forEachIndexed { index, title ->
            DropdownMenuItem(onClick = {
                onSelectedAngleIndexChange(index)
                onDismissRequest()
            }) {
                Text(title)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownMenuItem(
    onClick: () -> Unit, content: @Composable () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(Color.White, shape = RoundedCornerShape(2.dp))
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    onClick()
                }
            }
            true
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            content()
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun AngleDisplay(angle: Double?) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = Color.White, shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(text = stringResource(
            R.string.angle_found,
            angle?.let { String.format("%.1f", it) } ?: stringResource(R.string.angle_placeholder)
        ),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E88E5))
    }
}