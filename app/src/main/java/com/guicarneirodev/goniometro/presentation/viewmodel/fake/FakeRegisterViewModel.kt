package com.guicarneirodev.goniometro.presentation.viewmodel.fake

import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.validator.RegisterValidator
import com.guicarneirodev.goniometro.presentation.viewmodel.RegisterScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FakeRegisterViewModel : RegisterScreenViewModel() {
    private val registerValidator = RegisterValidator()

    override fun updateEmail(email: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    email = email,
                    emailError = if (registerValidator.isEmailValid(email)) "" else "Email inválido"
                )
            }
        }
    }

    override fun updatePassword(password: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    password = password,
                    passwordError = registerValidator.getPasswordError(password)
                )
            }
        }
    }

    override fun updateConfirmPassword(confirmPassword: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    confirmPassword = confirmPassword,
                    confirmPasswordError = registerValidator.passwordMatchError(
                        _uiState.value.password,
                        confirmPassword
                    )
                )
            }
        }
    }

    override fun registerUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(500)

            if (_uiState.value.email.isNotEmpty() &&
                _uiState.value.password.isNotEmpty() &&
                _uiState.value.confirmPassword.isNotEmpty() &&
                _uiState.value.emailError.isEmpty() &&
                _uiState.value.passwordError.isEmpty() &&
                _uiState.value.confirmPasswordError.isEmpty()
            ) {
                _uiState.update { it.copy(isLoading = false) }
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Campos inválidos"
                    )
                }
                withContext(Dispatchers.Main) {
                    onError("Campos inválidos")
                }
            }
        }
    }
}