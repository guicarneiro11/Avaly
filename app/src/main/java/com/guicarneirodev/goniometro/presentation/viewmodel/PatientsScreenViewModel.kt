package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.repository.PatientRepository
import com.guicarneirodev.goniometro.data.service.PdfService
import com.guicarneirodev.goniometro.presentation.ui.screens.patients.Patient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}

class PatientsScreenViewModel(
    private val repository: PatientRepository,
    private val pdfService: PdfService
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredPatients: StateFlow<List<Patient>> =
        combine(_patients, _searchQuery) { patients, query ->
            if (query.isBlank()) patients else patients.filter {
                it.name.contains(
                    query,
                    ignoreCase = true
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.getPatients().collect {
                _patients.value = it
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addPatient(name: String, date: String) {
        viewModelScope.launch {
            repository.addPatient(Patient("", name, date))
        }
    }

    fun updatePatient(patient: Patient) {
        viewModelScope.launch {
            repository.updatePatient(patient)
        }
    }

    fun deletePatient(patientId: String) {
        viewModelScope.launch {
            try {
                repository.deletePatient(patientId)
                _uiState.value = UiState.Success("Paciente excluído com sucesso")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Erro ao excluir paciente: ${e.message}")
            }
        }
    }

    fun sendPdfToEmail(userId: String, patientId: String, email: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                pdfService.sendPdfToEmail(userId, patientId, email)
                _uiState.value = UiState.Success("PDF enviado com sucesso para $email")
                delay(3000)
                _uiState.value = UiState.Idle
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Erro ao enviar PDF: ${e.message}")
            }
        }
    }
}