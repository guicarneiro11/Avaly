package com.guicarneirodev.goniometro.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.Patient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientDialog(
    patient: Patient,
    onDismiss: () -> Unit,
    onSave: (Patient) -> Unit
) {
    var editedName by remember { mutableStateOf(patient.name) }
    var editedDate by remember { mutableStateOf(patient.evaluationDate) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Paciente") },
        text = {
            Column {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Nome") }
                )
                Button(onClick = { showDatePicker = !showDatePicker }) {
                    Text(if (showDatePicker) "Ocultar Calendário" else "Mostrar Calendário")
                }
                if (showDatePicker) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
                datePickerState.selectedDateMillis?.let { millis ->
                    val localDate = Date(millis)
                    val timeZone = TimeZone.getDefault()
                    val calendar = Calendar.getInstance(timeZone)
                    calendar.time = localDate
                    calendar.add(Calendar.DAY_OF_MONTH, 1) // Adiciona um dia à data
                    editedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                    Text("Data selecionada: $editedDate", fontSize = 18.sp)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(patient.copy(name = editedName, evaluationDate = editedDate))
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun SendPdfDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enviar PDF por E-mail") },
        text = {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        },
        confirmButton = {
            Button(onClick = { if (email.isNotBlank()) onSend(email) }) {
                Text("Enviar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    var formattedDate by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Paciente") },
        text = {
            Column {
                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text("Nome") }
                )
                Button(onClick = { showDatePicker = !showDatePicker }) {
                    Text(if (showDatePicker) "Ocultar Calendário" else "Mostrar Calendário")
                }
                if (showDatePicker) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false,
                        colors = DatePickerDefaults.colors(
                            weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                            selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                            selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
                datePickerState.selectedDateMillis?.let { millis ->
                    val localDate = Date(millis)
                    val timeZone = TimeZone.getDefault()
                    val calendar = Calendar.getInstance(timeZone)
                    calendar.time = localDate
                    calendar.add(Calendar.DAY_OF_MONTH, 1) // Adiciona um dia à data
                    formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
                    Text("Data selecionada: $formattedDate", fontSize = 18.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (patientName.isNotBlank() && formattedDate.isNotBlank()) {
                        onAdd(patientName, formattedDate)
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}