package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun MenuItems(
    navController: NavController,
    onDismiss: () -> Unit,
    onImportImage: () -> Unit,
    onCaptureImage: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid ?: ""

    var dialogOpen by remember { mutableStateOf(false) }

    if (dialogOpen) {
        InstructionsDialog(onDismiss = { dialogOpen = false })
    }

    MenuItem(
        icon = painterResource(id = R.drawable.clinical_notes),
        text = stringResource(R.string.patients),
        onClick = {
            navController.navigate("patients/$userId")
            onDismiss()
        }
    )

    MenuItem(
        icon = painterResource(id = R.drawable.photo_library),
        text = stringResource(R.string.import_photo),
        onClick = onImportImage
    )

    MenuItem(
        icon = painterResource(id = R.drawable.addphoto),
        text = stringResource(R.string.capture_photo),
        onClick = onCaptureImage
    )

    MenuItem(
        icon = painterResource(id = R.drawable.help),
        text = stringResource(R.string.help),
        onClick = {
            dialogOpen = true
        }
    )
    if (dialogOpen) {
        InstructionsDialog(onDismiss = { dialogOpen = false })
    }

    MenuItem(icon = painterResource(id = R.drawable.logout), text = stringResource(R.string.exit), onClick = {
        navController.navigate("selection") {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
        }
        onDismiss()
    })
}

@Composable
fun MenuItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        color = PrimaryLight
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.size(24.dp),
                tint = AccentBlue
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = AccentBlue,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
