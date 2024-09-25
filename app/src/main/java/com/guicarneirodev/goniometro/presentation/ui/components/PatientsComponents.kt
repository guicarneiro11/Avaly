package com.guicarneirodev.goniometro.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.Patient
import com.guicarneirodev.goniometro.R

@Composable
fun PatientItem(
    patient: Patient,
    onEdit: (Patient) -> Unit,
    onDelete: (String) -> Unit,
    onSendPdf: (String) -> Unit,
    onNavigateToResults: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${patient.name}: ${patient.evaluationDate}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onSendPdf(patient.id) }) {
            Icon(
                painter = painterResource(id = R.drawable.reportpdf),
                contentDescription = "Enviar PDF por email",
                tint = Color(0xFF000000)
            )
        }
        IconButton(onClick = { onEdit(patient) }) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Editar",
                tint = Color(0xFF000000)
            )
        }
        IconButton(onClick = { onDelete(patient.id) }) {
            Icon(
                painter = painterResource(id = R.drawable.delete),
                contentDescription = "Deletar",
                tint = Color(0xFF000000)
            )
        }
        IconButton(onClick = { onNavigateToResults(patient.id) }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Resultados",
                tint = Color(0xFF000000)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientAppBar(
    navController: NavController,
    onAddPatient: (String, String) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Buscar paciente") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE6E6E6)),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.voltar),
                    contentDescription = "Voltar Tela",
                    tint = Color(0xFF000000)
                )
            }
        },
        actions = {
            IconButton(onClick = { showAddDialog = true }) {
                Icon(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = "Add",
                    tint = Color(0xFF000000)
                )
            }
        }
    )

    if (showAddDialog) {
        AddPatientDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, date ->
                onAddPatient(name, date)
                showAddDialog = false
            }
        )
    }
}