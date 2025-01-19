package com.guicarneirodev.goniometro.presentation.viewmodel.fake

import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.repository.fake.FakeLoginRepository
import com.guicarneirodev.goniometro.domain.repository.fake.FakePreferencesRepository
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginUiState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FakeLoginViewModel : LoginScreenViewModel(FakeLoginRepository(), FakePreferencesRepository()) {
    fun simulateSuccessfulLogin() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    navigateToSelection = true,
                    isLoading = false,
                    errorMessage = null
                )
            }
        }
    }

    // Expose uiState for debugging
    override val uiState: StateFlow<LoginUiState>
        get() = _uiState.asStateFlow()
}