package com.guicarneirodev.goniometro

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.text.SimpleDateFormat
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
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Add",
                        tint = Color(0xFF000000)
                    )
                }
            }
        )
    }
}

interface SendPdfApi {
    @GET("api/users/{userId}/patients/{patientId}/send-pdf")
    suspend fun sendPdfToEmail(
        @Path("userId") userId: String,
        @Path("patientId") patientId: String,
        @Query("email") email: String
    ): Response<Void>
}

object RetrofitInstance {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ktor-app-cc5gi2t6tq-rj.a.run.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SendPdfApi by lazy {
        retrofit.create(SendPdfApi::class.java)
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

    var showEmailDialog by remember { mutableStateOf(false) }
    var emailToSend by remember { mutableStateOf("") }
    var currentPatientId by remember { mutableStateOf("") }

    val db = Firebase.firestore
    val docRef = db.collection("users").document(userId).collection("patients")

    val context = LocalContext.current

    val filteredPatients = searchQuery.takeIf { it.isNotEmpty() }
        ?.let { query -> patients.filter { it.second.contains(query, ignoreCase = true) } }
        ?: patients

    LaunchedEffect(key1 = userId) {
        docRef.addSnapshotListener { snapshot, e ->
            e?.let {
                Log.w("Firestore", "Listen failed.", it)
                return@addSnapshotListener
            }

            snapshot?.takeIf { !it.isEmpty }?.let {
                val items = it.documents.map { document ->
                    Triple(
                        document.id,
                        document["patientName"] as? String ?: "",
                        document["evaluationDate"] as? String ?: "01/01/2024"
                    )
                }
                patients.apply {
                    clear()
                    addAll(items)
                }
            } ?: run {
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

        docRef.add(newPatient).addOnCompleteListener { task ->
            task.result?.let { docRef ->
                patients.add(Triple(docRef.id, patient, date))
                docRef.collection("angles")
            } ?: Log.e("Firestore", "Error adding document", task.exception)
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

    fun sendPdfToEmail(userId: String, patientId: String, email: String, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = try {
                val response = RetrofitInstance.api.sendPdfToEmail(userId, patientId, email)
                response.takeIf { it.isSuccessful }?.let {
                    "E-mail enviado com sucesso!"
                } ?: "Erro ao enviar e-mail: ${response.errorBody()?.string()}"
            } catch (e: Exception) {
                "Erro ao enviar e-mail: ${e.message}"
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
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
                                currentPatientId = triple.first
                                showEmailDialog = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.reportpdf),
                                    contentDescription = "Enviar PDF por email",
                                    tint = Color(0xFF000000)
                                )
                            }
                            IconButton(onClick = {
                                startEditing(index, patientName, evaluationDate)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit),
                                    contentDescription = "Editar",
                                    tint = Color(0xFF000000)
                                )
                            }
                            IconButton(onClick = { deletePatient(docId) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = "Deletar",
                                    tint = Color(0xFF000000)
                                )
                            }
                            IconButton(onClick = { navController.navigate("results/$userId/${triple.first}") }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow),
                                    contentDescription = "Resultados",
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
                            Text(showDatePicker.takeIf { it }?.let { "Ocultar Calendário" }
                                ?: "Mostrar Calendário")
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
    if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = {
                showEmailDialog = false
                emailToSend = ""
            },
            title = { Text("Enviar PDF por E-mail") },
            text = {
                OutlinedTextField(
                    value = emailToSend,
                    onValueChange = { emailToSend = it },
                    label = { Text("E-mail") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (emailToSend.isNotBlank()) {
                        sendPdfToEmail(userId, currentPatientId, emailToSend, context)
                        showEmailDialog = false
                        emailToSend = ""
                    }
                }) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showEmailDialog = false
                    emailToSend = ""
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}