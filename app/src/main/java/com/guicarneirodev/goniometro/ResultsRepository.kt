package com.guicarneirodev.goniometro

import kotlinx.coroutines.flow.Flow

data class AngleData(val id: String, val name: String, val value: String)

interface ResultsRepository {
    suspend fun getAngles(): Flow<List<AngleData>>
    suspend fun addAngle(name: String, value: String)
    suspend fun updateAngle(docId: String, newName: String, newValue: String)
    suspend fun deleteAngle(docId: String)
}