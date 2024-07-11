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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
class ResultsAppBar () {
    @Composable
    fun AppBar(
        navController: NavController,
        addAngle: (String, String) -> Unit,
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit
    ) {
        val focusManager = LocalFocusManager.current

        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Buscar articulação") },
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
                var showDialog by remember { mutableStateOf(false) }
                var articulationName by remember { mutableStateOf("") }
                var angleValue by remember { mutableStateOf("") }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Adicionar Paciente") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = articulationName,
                                    onValueChange = { articulationName = it },
                                    label = { Text("Nome da articulação") }
                                )
                                OutlinedTextField(
                                    value = angleValue,
                                    onValueChange = { newValue ->
                                        val cleanedValue = newValue.replace("°", "")
                                        angleValue = if (cleanedValue.isNotEmpty()) "$cleanedValue°" else ""
                                    },
                                    label = { Text("Valor encontrado") }
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = {
                                if (articulationName.isNotBlank() && angleValue.isNotBlank()) {
                                    addAngle(articulationName, angleValue)
                                    showDialog = false
                                    articulationName = ""
                                    angleValue = ""
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
                    Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add")
                }
            }
        )
    }
}

@Composable
fun Results(navController: NavController, userId: String, patientId: String) {
    val angles = remember { mutableStateListOf<Triple<String, String, String>>() }
    var currentlyEditing by remember { mutableIntStateOf(-1) }
    var editName by remember { mutableStateOf("") }
    var editValue by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }
    var docIdToEdit by remember { mutableStateOf("") }

    val db = Firebase.firestore
    val docRef = db.collection("users").document(userId).collection("patients").document(patientId).collection("results")

    LaunchedEffect(key1 = patientId) {
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val items = snapshot.documents.map {
                    val id = it.id
                    val name = it["name"] as? String ?: throw IllegalStateException("Campo 'name' não encontrado ou nulo.")
                    val value = it["value"] as? String ?: "Valor padrão"
                    Triple(id, name, value)
                }
                angles.clear()
                angles.addAll(items)
            } else {
                Log.d("Firestore", "Current data: null")
            }
        }
    }

    fun addAngle(name: String, value: String) {
        val newAngle = hashMapOf("name" to name, "value" to value, "created" to FieldValue.serverTimestamp())
        docRef.add(newAngle).addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error adding document", it.exception)
            } else {
                it.result?.let { docRef ->
                    angles.add(Triple(docRef.id, name, value))
                }
            }
        }
    }

    fun updateAngle(docId: String, newName: String, newValue: String) {
        docRef.document(docId).update(mapOf("name" to newName, "value" to newValue)).addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error updating document", it.exception)
            }
        }
    }

    fun deleteAngle(docId: String) {
        docRef.document(docId).delete().addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error deleting document", it.exception)
            }
        }
    }

    val filteredAngles = if (searchQuery.isEmpty()) {
        angles
    } else {
        angles.filter { it.second.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = { ResultsAppBar().AppBar(navController, ::addAngle, searchQuery) { searchQuery = it }
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
                    itemsIndexed(filteredAngles) { index, triple ->
                        val (docId, angleName, angleValue) = triple
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$angleName: $angleValue",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                currentlyEditing = index
                                editName = angleName
                                editValue = angleValue
                                docIdToEdit = docId
                                showEditDialog = true
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit),
                                    contentDescription = "Edit"
                                )
                            }
                            IconButton(onClick = { deleteAngle(docId) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.delete),
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Editar Goniometria") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nome da articulação") }
                    )
                    OutlinedTextField(
                        value = editValue,
                        onValueChange = { newValue ->
                            val cleanedValue = newValue.replace("°", "")
                            editValue = if (cleanedValue.isNotEmpty()) "$cleanedValue°" else ""
                        },
                        label = { Text("Valor encontrado") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (editName.isNotBlank() && editValue.isNotBlank()) {
                        updateAngle(docIdToEdit, editName, editValue)
                        showEditDialog = false
                        editName = ""
                        editValue = ""
                        currentlyEditing = -1
                    }
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