package com.guicarneirodev.goniometro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.navigation.NavController

data class Patient(
    val id: String,
    val name: String,
    val evaluationDate: String
)

@Composable
fun PatientsScreen(
    viewModel: PatientViewModel,
    navController: NavController,
    userId: String
) {
    val patients by viewModel.filteredPatients.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var currentlyEditingPatient by remember { mutableStateOf<Patient?>(null) }

    var showEmailDialog by remember { mutableStateOf(false) }
    var emailToSend by remember { mutableStateOf("") }
    var currentPatientId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            PatientAppBar(
                navController = navController,
                onAddPatient = { name, date -> viewModel.addPatient(name, date) },
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::setSearchQuery
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFCBE3FF), Color(0xFFCBE3FF))
                    )
                )
        ) {
            LazyColumn {
                items(patients) { patient ->
                    PatientItem(
                        patient = patient,
                        onEdit = {
                            currentlyEditingPatient = it
                            showEditDialog = true
                        },
                        onDelete = viewModel::deletePatient,
                        onSendPdf = { patientId ->
                            currentPatientId = patientId
                            showEmailDialog = true
                        },
                        onNavigateToResults = { patientId ->
                            navController.navigate("results/$userId/$patientId")
                        }
                    )
                }
            }
        }
    }

    if (showEditDialog) {
        EditPatientDialog(
            patient = currentlyEditingPatient!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedPatient ->
                viewModel.updatePatient(updatedPatient)
                showEditDialog = false
            }
        )
    }

    if (showEmailDialog) {
        SendPdfDialog(
            onDismiss = {
                showEmailDialog = false
                emailToSend = ""
            },
            onSend = { email ->
                viewModel.sendPdfToEmail(userId, currentPatientId, email)
                showEmailDialog = false
                emailToSend = ""
            }
        )
    }
}