package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserProfile
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.domain.usecase.GetAvailableToolsUseCase
import com.guicarneirodev.goniometro.domain.usecase.GetUserPreferencesUseCase
import com.guicarneirodev.goniometro.domain.usecase.SaveUserPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.guicarneirodev.goniometro.utils.LocaleHelper

data class SelectionUiState(
    val tools: List<Tool> = emptyList(),
    val userProfile: UserProfile? = null,
    val userPreferences: UserPreferences = UserPreferences(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SelectionViewModel(
    private val localeHelper: LocaleHelper,
    private val getAvailableToolsUseCase: GetAvailableToolsUseCase,
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val saveUserPreferencesUseCase: SaveUserPreferencesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SelectionUiState())
    val uiState: StateFlow<SelectionUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val tools = getAvailableToolsUseCase()
                val preferences = getUserPreferencesUseCase()
                _uiState.update {
                    it.copy(
                        tools = tools,
                        userPreferences = preferences,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun updateUserType(userType: UserType) {
        viewModelScope.launch {
            val currentPreferences = _uiState.value.userPreferences
            val newPreferences = currentPreferences.copy(userType = userType)
            saveUserPreferencesUseCase(newPreferences)
            _uiState.update { it.copy(userPreferences = newPreferences) }
        }
    }

    fun updateLanguage(language: Language) {
        viewModelScope.launch {
            if (localeHelper.updateLocale(language)) {
                val currentPreferences = _uiState.value.userPreferences
                val newPreferences = currentPreferences.copy(language = language)
                saveUserPreferencesUseCase(newPreferences)
                _uiState.update { it.copy(userPreferences = newPreferences) }
            }
        }
    }
}