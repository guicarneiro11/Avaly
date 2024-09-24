package com.guicarneirodev.goniometro

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ResultsRepositoryImpl(
    userId: String,
    patientId: String
) : ResultsRepository {
    private val db = Firebase.firestore
    private val resultsCollection = db.collection("users").document(userId)
        .collection("patients").document(patientId).collection("results")

    override suspend fun getAngles(): Flow<List<AngleData>> = callbackFlow {
        val listener = resultsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }

            val angles = snapshot?.documents?.mapNotNull { doc ->
                AngleData(
                    id = doc.id,
                    name = doc.getString("name") ?: return@mapNotNull null,
                    value = doc.getString("value") ?: "Valor padrão"
                )
            } ?: emptyList()

            trySend(angles)
        }

        awaitClose { listener.remove() }
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
        resultsCollection.document(docId).update(mapOf(
            "name" to newName,
            "value" to newValue
        )).await()
    }

    override suspend fun deleteAngle(docId: String) {
        resultsCollection.document(docId).delete().await()
    }
}