package com.guicarneirodev.goniometro.presentation.viewmodel.fake

import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Tool
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.presentation.ui.screens.selection.SelectionViewModelInterface
import com.guicarneirodev.goniometro.presentation.viewmodel.SelectionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeSelectionViewModel(
    initialTools: List<Tool> = emptyList()
) : SelectionViewModelInterface {
    private val _uiState = MutableStateFlow(
        SelectionUiState(
            tools = initialTools,
            userPreferences = UserPreferences(userId = "test@email.com"),
            isLoading = false
        )
    )
    override val uiState: StateFlow<SelectionUiState> = _uiState.asStateFlow()

    override fun updateLanguage(language: Language) {
        _uiState.update {
            it.copy(userPreferences = it.userPreferences.copy(language = language))
        }
    }

    override fun updateUserType(userType: UserType) {
        _uiState.update {
            it.copy(userPreferences = it.userPreferences.copy(userType = userType))
        }
    }
}