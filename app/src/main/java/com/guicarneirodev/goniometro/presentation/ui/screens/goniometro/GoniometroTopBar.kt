package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    angleOptions: List<String>,
    angleDropdownExpanded: Boolean,
    menuDropdownExpanded: Boolean,
    onAngleDropdownExpandedChange: (Boolean) -> Unit,
    onMenuDropdownExpandedChange: (Boolean) -> Unit,
    onSelectedAngleIndexChange: (Int) -> Unit,
    onImportImage: () -> Unit,
    onCaptureImage: () -> Unit,
    onDialogOpenChange: (Boolean) -> Unit,
    navController: NavController,
    userId: String
) {
    TopAppBar(
        title = { Text("") },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE6E6E6)),
        actions = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(onClick = { onMenuDropdownExpandedChange(true) }, Modifier.padding(1.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier.size(44.dp),
                        tint = Color(0xFF000000)
                    )
                }
                DropdownMenu(
                    expanded = menuDropdownExpanded,
                    onDismissRequest = { onMenuDropdownExpandedChange(false) },
                    modifier = Modifier
                        .padding(1.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    DropdownMenuItem(onClick = { onAngleDropdownExpandedChange(true) }) {
                        MenuItemContent(
                            iconId = R.drawable.tibia,
                            text = "Alterar Quadrante"
                        )
                        AngleDropdownMenu(
                            expanded = angleDropdownExpanded,
                            onDismissRequest = { onAngleDropdownExpandedChange(false) },
                            angleOptions = angleOptions,
                            onSelectedAngleIndexChange = onSelectedAngleIndexChange
                        )
                    }
                    DropdownMenuItem(onClick = {
                        navController.navigate("patients/$userId")
                        onMenuDropdownExpandedChange(false)
                    }) {
                        MenuItemContent(
                            iconId = R.drawable.clinical_notes,
                            text = "Pacientes"
                        )
                    }
                    DropdownMenuItem(onClick = {
                        onImportImage()
                        onMenuDropdownExpandedChange(false)
                    }) {
                        MenuItemContent(
                            iconId = R.drawable.photo_library,
                            text = "Importar Foto"
                        )
                    }
                    DropdownMenuItem(onClick = {
                        onCaptureImage()
                    }) {
                        MenuItemContent(
                            iconId = R.drawable.addphoto,
                            text = "Capturar Foto"
                        )
                    }
                    DropdownMenuItem(onClick = {
                        onDialogOpenChange(true)
                    }) {
                        MenuItemContent(
                            iconId = R.drawable.help,
                            text = "Ajuda"
                        )
                    }
                    DropdownMenuItem(onClick = {
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                        onMenuDropdownExpandedChange(false)
                    }) {
                        MenuItemContent(
                            iconId = R.drawable.logout,
                            text = "Logout"
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
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
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            content()
        }
    }
}

@Composable
fun MenuItemContent(iconId: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF000000)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF000000),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun AngleDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    angleOptions: List<String>,
    onSelectedAngleIndexChange: (Int) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
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