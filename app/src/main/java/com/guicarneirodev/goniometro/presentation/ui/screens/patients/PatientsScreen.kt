package com.guicarneirodev.goniometro.presentation.ui.screens.patients

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.ui.reusable.SearchField
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.components.AddPatientDialog
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.components.EditPatientDialog
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.components.SendPdfDialog
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.components.PatientCard
import com.guicarneirodev.goniometro.presentation.viewmodel.PatientsScreenViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.UiState
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

data class Patient(
    val id: String,
    val name: String,
    val evaluationDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientsScreen(
    viewModel: PatientsScreenViewModel,
    navController: NavController,
    userId: String
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val patients by viewModel.filteredPatients.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var currentlyEditingPatient by remember { mutableStateOf<Patient?>(null) }
    var showEmailDialog by remember { mutableStateOf(false) }
    var currentPatientId by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.patients),
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
                    var showAddDialog by remember { mutableStateOf(false) }
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_action),
                            tint = AccentBlue
                        )
                    }
                    if (showAddDialog) {
                        AddPatientDialog(
                            onDismiss = { showAddDialog = false },
                            onAdd = { name, date ->
                                viewModel.addPatient(name, date)
                                showAddDialog = false
                            }
                        )
                    }
                }
            )
        },
        containerColor = SecondaryDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(SecondaryDark)
        ) {
            SearchField(
                value = searchQuery,
                onValueChange = viewModel::setSearchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(patients) { patient ->
                    PatientCard(
                        patient = patient,
                        onEdit = {
                            currentlyEditingPatient = it
                            showEditDialog = true
                        },
                        onDelete = viewModel::deletePatient,
                        onSendPdf = {
                            currentPatientId = it
                            showEmailDialog = true
                        },
                        onNavigateToResults = { patientId ->
                            navController.navigate("results/$userId/$patientId")
                        }
                    )
                }
            }
        }

        LaunchedEffect(uiState) {
            when (uiState) {
                is UiState.Success -> {
                    snackbarHostState.showSnackbar(
                        message = (uiState as UiState.Success).message,
                        duration = SnackbarDuration.Short
                    )
                }
                is UiState.Error -> {
                    snackbarHostState.showSnackbar(
                        message = (uiState as UiState.Error).message,
                        duration = SnackbarDuration.Long
                    )
                }
                else -> {}
            }
        }
    }

    if (showEditDialog) {
        currentlyEditingPatient?.let { patient ->
            EditPatientDialog(
                patient = patient,
                onDismiss = {
                    showEditDialog = false
                    currentlyEditingPatient = null
                },
                onSave = { updatedPatient ->
                    viewModel.updatePatient(updatedPatient)
                    showEditDialog = false
                    currentlyEditingPatient = null
                }
            )
        }
    }

    if (showEmailDialog) {
        SendPdfDialog(
            onDismiss = {
                showEmailDialog = false
                currentPatientId = ""
            },
            onSend = { email ->
                viewModel.sendPdfToEmail(userId, currentPatientId, email)
            },
            uiState = uiState
        )
    }
}