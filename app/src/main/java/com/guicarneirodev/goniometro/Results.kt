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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController, angles: SnapshotStateList<Pair<String, String>>, addAngle: (String) -> Unit) {
    TopAppBar(
        title = { Text("Goniometrias") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrowback),
                    contentDescription = "Voltar Tela"
                )
            }
        },
        actions = {
            IconButton(onClick = { addAngle("Ângulo ${angles.size + 1}") }) {
                Icon(painter = painterResource(id = R.drawable.add), contentDescription = "Add")
            }
        }
    )
}

@Composable
fun Angles(navController: NavController, userId: String) {
    val angles = remember { mutableStateListOf<Pair<String, String>>() }
    var currentlyEditing by remember { mutableIntStateOf(-1) }
    var editText by remember { mutableStateOf("") }
    val db = Firebase.firestore
    val docRef = db.collection("users").document(userId).collection("angles")

    LaunchedEffect(key1 = userId) {
        docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val items = snapshot.documents.map { it.id to (it.getString("angle") ?: "") }
                    angles.clear()
                    angles.addAll(items)
                } else {
                    Log.d("Firestore", "Current data: null")
                }
            }
    }

    fun addAngle(angle: String) {
        val newAngle = hashMapOf("angle" to angle, "created" to FieldValue.serverTimestamp())
        docRef.add(newAngle).addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.e("Firestore", "Error adding document", it.exception)
            } else {
                it.result?.let { docRef ->
                    angles.add(docRef.id to angle)
                }
            }
        }
    }

    fun updateAngle(docId: String, newAngle: String) {
        docRef.document(docId).update("angle", newAngle).addOnCompleteListener {
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
        topBar = { MyTopAppBar(navController, angles, ::addAngle) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
        ) {
            Column {
                LazyColumn {
                    itemsIndexed(angles) { index, pair ->
                        val (docId, angleText) = pair
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (index == currentlyEditing) {
                                TextField(
                                    value = editText,
                                    onValueChange = { editText = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                IconButton(onClick = {
                                    updateAngle(docId, editText)
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
                                    text = angleText,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    currentlyEditing = index
                                    editText = angleText
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