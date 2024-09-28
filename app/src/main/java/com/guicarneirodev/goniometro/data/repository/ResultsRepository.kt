package com.guicarneirodev.goniometro.data.repository

import kotlinx.coroutines.flow.StateFlow

data class AngleData(
    val id: String,
    val name: String,
    val value: String
)

interface ResultsRepository {
    val angles: StateFlow<List<AngleData>>
    suspend fun addAngle(name: String, value: String)
    suspend fun updateAngle(docId: String, newName: String, newValue: String)
    suspend fun deleteAngle(docId: String)
}