package com.guicarneirodev.goniometro

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class PatientAppBar() {
    @Composable
    fun AppBar(
        navController: NavController,
        addPatient: (String, String) -> Unit,
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit
    ) {
        val focusManager = LocalFocusManager.current

        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Buscar paciente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                        focusManager.clearFocus()
                        }
                    )
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
                var showDialog by remember { mutableStateOf(false) }
                var patientName by remember { mutableStateOf("") }
                val datePickerState = rememberDatePickerState(
                    initialDisplayMode = DisplayMode.Input
                )
                var formattedDate by remember { mutableStateOf("") }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Adicionar Paciente") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = patientName,
                                    onValueChange = { patientName = it },
                                    label = { Text("Nome") }
                                )
                                DatePicker(
                                    state = datePickerState,
                                    showModeToggle = false,
                                    colors = DatePickerDefaults.colors(
                                        weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                                        selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                                formattedDate = datePickerState.selectedDateMillis?.let {
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                        Date(
                                            it
                                        )
                                    )
                                } ?: ""
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                if (patientName.isNotBlank() && formattedDate.isNotBlank()) {
                                    addPatient(patientName, formattedDate)
                                    showDialog = false
                                    patientName = ""
                                    formattedDate = ""
                                }
                            }) {
                                Text("Adicionar")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add",
                        tint = Color(0xFF000000)
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Patients(navController: NavController, userId: String) {
    val patients = remember { mutableStateListOf<Triple<String, String, String>>() }
    var currentlyEditing by remember { mutableIntStateOf(-1) }
    var editPatient by remember { mutableStateOf("") }
    var editDate by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }

    val db = Firebase.firestore
    val docRef = db.collection("users").document(userId).collection("patients")

    LaunchedEffect(key1 = userId) {
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val items = snapshot.documents.map {
                    val id = it.id
                    val patientName = it["patientName"] as? String ?: ""
                    val evaluationDate = it["evaluationDate"] as? String ?: "01/01/2024"
                    Triple(id, patientName, evaluationDate)
                }
                patients.clear()
                patients.addAll(items)
            } else {
                Log.d("Firestore", "Current data: null")
            }
        }
    }

    fun addPatient(patient: String, date: String) {
        val newPatient = hashMapOf(
            "patientName" to patient,
            "evaluationDate" to date,
            "created" to FieldValue.serverTimestamp()
        )
        docRef.add(newPatient).addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error adding document", it.exception)
            } else {
                it.result?.let { docRef ->
                    patients.add(Triple(docRef.id, patient, date))
                    docRef.collection("angles")
                }
            }
        }
    }

    fun updatePatient(docId: String, newPatient: String, newDate: String) {
        docRef.document(docId)
            .update(mapOf("patientName" to newPatient, "evaluationDate" to newDate))
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.e("Firestore", "Error updating document", it.exception)
                }
            }
    }

    fun startEditing(index: Int, patientName: String, evaluationDate: String) {
        currentlyEditing = index
        editPatient = patientName
        editDate = evaluationDate
        showEditDialog = true
    }

    fun saveEdit(docId: String, newPatient: String, newDate: String) {
        updatePatient(docId, newPatient, newDate)
        currentlyEditing = -1
        showEditDialog = false
    }

    fun deletePatient(docId: String) {
        docRef.document(docId).delete().addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error deleting document", it.exception)
            }
        }
    }

    val filteredPatients = if (searchQuery.isEmpty()) {
        patients
    } else {
        patients.filter { it.second.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            PatientAppBar().AppBar(navController, ::addPatient, searchQuery) { searchQuery = it }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFCBE3FF),
                            Color(0xFFCBE3FF)
                        )
                    )
                )
        ) {
            Column {
                LazyColumn {
                    itemsIndexed(filteredPatients) { index, triple ->
                        val (docId, patientName, evaluationDate) = triple
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$patientName: $evaluationDate",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                startEditing(index, patientName, evaluationDate)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit),
                                    contentDescription = "Edit",
                                    tint = Color(0xFF000000)
                                )
                            }
                            IconButton(onClick = { deletePatient(docId) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = "Delete",
                                    tint = Color(0xFF000000)
                                )
                            }
                            IconButton(onClick = { navController.navigate("results/$userId/${triple.first}") }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow),
                                    contentDescription = "Arrow",
                                    tint = Color(0xFF000000)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showEditDialog) {
            var showDatePicker by remember { mutableStateOf(false) }
            val editDatePickerState = rememberDatePickerState(
                initialDisplayMode = DisplayMode.Input
            )
            var formattedEditDate by remember(editDatePickerState.selectedDateMillis) {
                mutableStateOf(
                    editDatePickerState.selectedDateMillis?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                    } ?: editDate
                )
            }

            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Editar Paciente") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = editPatient,
                            onValueChange = { editPatient = it },
                            label = { Text("Nome") }
                        )

                        Button(onClick = { showDatePicker = !showDatePicker }) {
                            Text(if (showDatePicker) "Ocultar Calendário" else "Mostrar Calendário")
                        }

                        if (showDatePicker) {
                            DatePicker(
                                state = editDatePickerState,
                                showModeToggle = false,
                                colors = DatePickerDefaults.colors(
                                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }

                        if (formattedEditDate.isNotBlank()) {
                            Text("Data selecionada: $formattedEditDate", fontSize = 18.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val docId = filteredPatients[currentlyEditing].first
                        saveEdit(docId, editPatient, formattedEditDate)
                    }) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showEditDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}