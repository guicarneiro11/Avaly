package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.data.repository.AngleData
import com.guicarneirodev.goniometro.data.repository.ResultsRepository
import kotlinx.coroutines.launch

class ResultsScreenViewModel  (
    private val repository: ResultsRepository
) : ViewModel() {
    private val _angles = mutableStateListOf<AngleData>()
    private val angles: List<AngleData> = _angles

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getFilteredAngles(): List<AngleData> {
        return searchQuery.value.takeIf { it.isNotEmpty() }
            ?.let { query -> angles.filter { it.name.contains(query, ignoreCase = true) } }
            ?: angles
    }

    fun addAngle(name: String, value: String) {
        viewModelScope.launch {
            repository.addAngle(name, value)
        }
    }

    fun updateAngle(docId: String, newName: String, newValue: String) {
        viewModelScope.launch {
            repository.updateAngle(docId, newName, newValue)
        }
    }

    fun deleteAngle(docId: String) {
        viewModelScope.launch {
            repository.deleteAngle(docId)
        }
    }

    init {
        viewModelScope.launch {
            repository.getAngles().collect { newAngles ->
                _angles.clear()
                _angles.addAll(newAngles)
            }
        }
    }
}