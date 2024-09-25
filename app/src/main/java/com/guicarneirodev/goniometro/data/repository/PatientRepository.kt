package com.guicarneirodev.goniometro.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.Patient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface PatientRepository {
    suspend fun getPatients(): Flow<List<Patient>>
    suspend fun addPatient(patient: Patient)
    suspend fun updatePatient(patient: Patient)
    suspend fun deletePatient(patientId: String)
}

class FirestorePatientRepository(
    userId: String
) : PatientRepository {
    private val db = Firebase.firestore
    private val docRef = db.collection("users").document(userId).collection("patients")

    override suspend fun getPatients(): Flow<List<Patient>> = callbackFlow {
        val listener = docRef.addSnapshotListener { snapshot, e ->
            e?.let {
                close(it)
                return@addSnapshotListener
            }

            snapshot?.documents?.mapNotNull { doc ->
                doc.toPatient()
            }?.let { patients ->
                trySend(patients)
            }
        }

        awaitClose { listener.remove() }
    }

    override suspend fun addPatient(patient: Patient) {
        docRef.add(patient.toMap())
    }

    override suspend fun updatePatient(patient: Patient) {
        docRef.document(patient.id).update(patient.toMap())
    }

    override suspend fun deletePatient(patientId: String) {
        docRef.document(patientId).delete()
    }

    private fun DocumentSnapshot.toPatient(): Patient? {
        return Patient(
            id = id,
            name = getString("patientName") ?: return null,
            evaluationDate = getString("evaluationDate") ?: return null
        )
    }

    private fun Patient.toMap(): Map<String, Any> {
        return mapOf(
            "patientName" to name,
            "evaluationDate" to evaluationDate,
            "created" to FieldValue.serverTimestamp()
        )
    }
}