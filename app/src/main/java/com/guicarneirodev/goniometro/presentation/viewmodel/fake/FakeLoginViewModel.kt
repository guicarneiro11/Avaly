package com.guicarneirodev.goniometro.presentation.viewmodel.fake

import com.guicarneirodev.goniometro.domain.repository.fake.FakeLoginRepository
import com.guicarneirodev.goniometro.domain.repository.fake.FakePreferencesRepository
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel

class FakeLoginViewModel : LoginScreenViewModel(FakeLoginRepository(), FakePreferencesRepository()) {
    fun setLoading(loading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = loading)
    }

    fun simulateSuccessfulLogin() {
        _uiState.value = _uiState.value.copy(navigateToSelection = true)
    }
}