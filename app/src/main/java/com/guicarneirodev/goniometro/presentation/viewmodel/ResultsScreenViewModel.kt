package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.repository.ResultsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultsScreenViewModel(
    private val repository: ResultsRepository
) : ViewModel() {
    val angles = repository.angles

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
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
}