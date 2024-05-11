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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController, addAngle: (String, String) -> Unit) {
    TopAppBar(
        title = { Text("Goniometrias") },
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
                addAngle("Articulação", "0°")
            }) {
                Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add")
            }
        }
    )
}

@Composable
fun Angles(navController: NavController, userId: String) {
    val angles = remember { mutableStateListOf<Triple<String, String, String>>() }
    var currentlyEditing by remember { mutableIntStateOf(-1) }
    var editName by remember { mutableStateOf("") }
    var editValue by remember { mutableStateOf("") }

    val db = Firebase.firestore
    val docRef = db.collection("users").document(userId).collection("angles")

    LaunchedEffect(key1 = userId) {
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Firestore", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                val items = snapshot.documents.map {
                    Triple(it.id, it["name"] as String, it["value"] as String)
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
        docRef.document(docId).update( mapOf("name" to newName, "value" to newValue)).addOnCompleteListener {
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

    Scaffold(
        topBar = { MyTopAppBar(navController, ::addAngle) }
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
                    itemsIndexed(angles) { index, triple ->
                        val (docId, angleName, angleValue) = triple
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (index == currentlyEditing) {
                                OutlinedTextField(
                                    value = editName,
                                    onValueChange = { editName = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                OutlinedTextField(
                                    value = editValue,
                                    onValueChange = { editValue = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                IconButton(onClick = {
                                    updateAngle(docId, editName, editValue)
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
    }
}