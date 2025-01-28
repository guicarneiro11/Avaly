package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.R
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

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

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
            _uiState.value = UiState.Loading
            try {
                repository.deleteAngle(docId)
                _uiState.value = UiState.Success("Medição excluída com sucesso")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Erro ao excluir: ${e.message}")
            }
        }
    }

    fun resetUiState() {
        _uiState.value = UiState.Idle
    }
}