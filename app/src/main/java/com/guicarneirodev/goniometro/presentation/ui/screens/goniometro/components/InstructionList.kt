package com.guicarneirodev.goniometro.presentation.ui.screens.goniometro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun InstructionList() {
    val instructions = listOf(
        stringResource(R.string.tip_1),
        stringResource(R.string.tip_2),
        stringResource(R.string.tip_3),
        stringResource(R.string.tip_4),
        stringResource(R.string.tip_5),
        stringResource(R.string.tip_6),
        stringResource(R.string.tip_7),
        stringResource(R.string.tip_8),
        stringResource(R.string.tip_9),
        stringResource(R.string.tip_10),
        stringResource(R.string.tip_11),
        stringResource(R.string.observation)
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(
                color = PrimaryLight.copy(alpha = 0.95f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        items(instructions) { instruction ->
            Text(
                text = instruction,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                color = AccentBlue,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 12.dp),
                color = AccentBlue.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun InstructionsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(16.dp),
        title = {
            Text(
                text = stringResource(R.string.instructions),
                style = MaterialTheme.typography.headlineMedium,
                color = AccentBlue,
                fontWeight = FontWeight.Bold
            )
        },
        text = { InstructionList() },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    contentColor = PrimaryLight
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.close))
            }
        },
        containerColor = PrimaryLight,
        shape = RoundedCornerShape(16.dp)
    )
}