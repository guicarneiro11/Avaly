package com.guicarneirodev.goniometro.data.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.guicarneirodev.goniometro.domain.repository.AngleData
import com.guicarneirodev.goniometro.domain.repository.ResultsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

class ResultsRepositoryImpl(
    userId: String,
    patientId: String
) : ResultsRepository {
    private val db = Firebase.firestore
    private val resultsCollection = db.collection("users").document(userId)
        .collection("patients").document(patientId).collection("results")

    private val _angles = MutableStateFlow<List<AngleData>>(emptyList())
    override val angles: StateFlow<List<AngleData>> = _angles.asStateFlow()

    init {
        resultsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val angles = snapshot?.documents?.mapNotNull { doc ->
                AngleData(
                    id = doc.id,
                    name = doc.getString("name") ?: return@mapNotNull null,
                    value = doc.getString("value") ?: "Valor padrão"
                )
            } ?: emptyList()

            _angles.value = angles
        }
    }

    override suspend fun addAngle(name: String, value: String) {
        val newAngle = hashMapOf(
            "name" to name,
            "value" to value,
            "created" to FieldValue.serverTimestamp()
        )
        resultsCollection.add(newAngle).await()
    }

    override suspend fun updateAngle(docId: String, newName: String, newValue: String) {
        resultsCollection.document(docId).update(
            mapOf(
                "name" to newName,
                "value" to newValue
            )
        ).await()
    }

    override suspend fun deleteAngle(docId: String) {
        resultsCollection.document(docId).delete().await()
    }
}