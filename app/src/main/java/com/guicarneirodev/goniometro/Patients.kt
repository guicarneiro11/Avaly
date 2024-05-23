package com.guicarneirodev.goniometro

import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientAppBar(
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
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE6E6E6)),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.voltar),
                    contentDescription = "Voltar Tela"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                addPatient("Nome", "01/01/2024")
            }) {
                Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add")
            }
        }
    )
}

@Composable
fun Patients(navController: NavController, userId: String) {
    val patients = remember { mutableStateListOf<Triple<String, String, String>>() }
    var currentlyEditing by remember { mutableIntStateOf(-1) }
    var editPatient by remember { mutableStateOf("") }
    var editDate by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

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
                    val patient = it["patient"] as? String ?: throw IllegalStateException("Campo 'patient' não encontrado ou nulo.")
                    val date = it["date"] as? String ?: "01/01/2024"
                    Triple(id, patient, date)
                }
                patients.clear()
                patients.addAll(items)
            } else {
                Log.d("Firestore", "Current data: null")
            }
        }
    }

    fun addPatient(patient: String, date: String) {
        val newPatient = hashMapOf("patient" to patient, "date" to date, "created" to FieldValue.serverTimestamp())
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
        docRef.document(docId).update(mapOf("patient" to newPatient, "date" to newDate)).addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error updating document", it.exception)
            }
        }
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
            PatientAppBar(navController, ::addPatient, searchQuery) { searchQuery = it }
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
                            if (index == currentlyEditing) {
                                OutlinedTextField(
                                    value = editPatient,
                                    onValueChange = { editPatient = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                OutlinedTextField(
                                    value = editDate,
                                    onValueChange = { editDate = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                IconButton(onClick = {
                                    updatePatient(docId, editPatient, editDate)
                                    currentlyEditing = -1
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.done),
                                        contentDescription = "Done"
                                    )
                                }
                                IconButton(onClick = { currentlyEditing = -1 }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.close),
                                        contentDescription = "Close"
                                    )
                                }
                            } else {
                                Text(
                                    text = "$patientName: $evaluationDate",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    currentlyEditing = index
                                    editPatient = patientName
                                    editDate = evaluationDate
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.edit),
                                        contentDescription = "Edit"
                                    )
                                }
                                IconButton(onClick = { deletePatient(docId) }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.delete),
                                        contentDescription = "Delete"
                                    )
                                }
                                IconButton(onClick = { navController.navigate("results/$userId/${triple.first}") }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow),
                                        contentDescription = "Arrow"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}