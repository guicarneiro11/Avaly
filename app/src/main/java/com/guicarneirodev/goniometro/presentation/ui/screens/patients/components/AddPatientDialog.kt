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

@Composable
fun AddPatientDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Text(
                stringResource(R.string.add_patient),
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1E88E5),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text( stringResource(R.string.patient_name) ) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E88E5),
                        focusedLabelColor = Color(0xFF1E88E5)
                    )
                )

                DatePicker(
                    onDateSelected = { selectedDate = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (patientName.isNotBlank() && selectedDate.isNotBlank()) {
                        onAdd(patientName, selectedDate)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5)
                )
            ) {
                Text( stringResource(R.string.add) )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1E88E5)
                ),
                border = BorderStroke(1.dp, Color(0xFF1E88E5))
            ) {
                Text( stringResource(R.string.cancel) )
            }
        }
    )
}