package com.guicarneirodev.goniometro.presentation.ui.screens.results.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.domain.repository.AngleData
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import com.guicarneirodev.goniometro.utils.formatAngleValue

@Composable
fun EditDialog(
    angle: AngleData,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var editName by remember { mutableStateOf(angle.name) }
    var editValue by remember { mutableStateOf(angle.value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = PrimaryLight,
        title = {
            Text(
                text = stringResource(R.string.edit_goniometry),
                style = MaterialTheme.typography.headlineSmall,
                color = AccentBlue,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text(stringResource(R.string.articulation_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        focusedLabelColor = AccentBlue,
                        cursorColor = AccentBlue,
                        unfocusedBorderColor = SecondaryDark.copy(alpha = 0.3f),
                        unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = editValue,
                    onValueChange = { newValue ->
                        editValue = formatAngleValue(newValue)
                    },
                    label = { Text(stringResource(R.string.value_found)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        focusedLabelColor = AccentBlue,
                        cursorColor = AccentBlue,
                        unfocusedBorderColor = SecondaryDark.copy(alpha = 0.3f),
                        unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (editName.isNotBlank() && editValue.isNotBlank()) {
                        onConfirm(editName, editValue)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    contentColor = PrimaryLight
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    stringResource(R.string.save),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AccentBlue
                ),
                border = BorderStroke(1.dp, AccentBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    stringResource(R.string.cancel),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}