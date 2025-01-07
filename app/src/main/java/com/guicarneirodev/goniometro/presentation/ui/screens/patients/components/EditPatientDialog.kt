package com.guicarneirodev.goniometro.presentation.ui.screens.patients.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.Patient
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

@Composable
fun EditPatientDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onSave: (Patient) -> Unit
) {
    var editedName by remember { mutableStateOf(patient.name) }
    var editedDate by remember { mutableStateOf(patient.evaluationDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = PrimaryLight,
        title = {
            Text(
                stringResource(R.string.edit_patient),
                style = MaterialTheme.typography.headlineSmall,
                color = AccentBlue,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text(stringResource(R.string.patient_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        focusedLabelColor = AccentBlue,
                        unfocusedBorderColor = SecondaryDark.copy(alpha = 0.3f),
                        unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
                    )
                )

                DatePicker(
                    onDateSelected = { editedDate = it },
                    initialDate = editedDate
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (editedName.isNotBlank() && editedDate.isNotBlank()) {
                        onSave(patient.copy(name = editedName, evaluationDate = editedDate))
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    contentColor = PrimaryLight
                )
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AccentBlue
                ),
                border = BorderStroke(1.dp, AccentBlue)
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}