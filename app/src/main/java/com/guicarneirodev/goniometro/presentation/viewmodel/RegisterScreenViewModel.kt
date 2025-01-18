package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.repository.RegisterRepository
import com.guicarneirodev.goniometro.domain.validator.RegisterValidator
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val confirmPasswordError: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

open class RegisterScreenViewModel : ViewModel() {
    private val registerValidator = RegisterValidator()
    private val registerRepositoryImpl = RegisterRepository()

    val _uiState = MutableStateFlow(RegisterUiState())
    open val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _emailActions = MutableSharedFlow<String>()
    private val _passwordActions = MutableSharedFlow<String>()
    private val _confirmPasswordActions = MutableSharedFlow<String>()
    private val _registerActions = MutableSharedFlow<RegisterAction>()

    private data class RegisterAction(
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit
    )

    init {
        setupValidationHandlers()
    }

    @OptIn(FlowPreview::class)
    private fun setupValidationHandlers() {
        viewModelScope.launch {
            _emailActions
                .debounce(300)
                .collect { email ->
                    _uiState.update {
                        it.copy(
                            email = email,
                            emailError = if (registerValidator.isEmailValid(email)) "" else "Email inválido"
                        )
                    }
                }
        }

        viewModelScope.launch {
            _passwordActions
                .debounce(300)
                .collect { password ->
                    _uiState.update {
                        it.copy(
                            password = password,
                            passwordError = registerValidator.getPasswordError(password)
                        )
                    }
                }
        }

        viewModelScope.launch {
            _confirmPasswordActions
                .debounce(300)
                .collect { confirmPassword ->
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

        viewModelScope.launch {
            _registerActions
                .debounce(300)
                .collect { action ->
                    handleRegister(action.onSuccess, action.onError)
                }
        }
    }

    open fun updateEmail(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(email = email) }
            _emailActions.emit(email)
        }
    }

    open fun updatePassword(password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(password = password) }
            _passwordActions.emit(password)
        }
    }

    open fun updateConfirmPassword(confirmPassword: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(confirmPassword = confirmPassword) }
            _confirmPasswordActions.emit(confirmPassword)
        }
    }

    open fun registerUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _registerActions.emit(RegisterAction(onSuccess, onError))
        }
    }

    private suspend fun handleRegister(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentState = _uiState.value

        if (currentState.emailError.isEmpty() &&
            currentState.passwordError.isEmpty() &&
            currentState.confirmPasswordError.isEmpty()
        ) {

            _uiState.update { it.copy(isLoading = true, errorMessage = "") }

            try {
                val result =
                    registerRepositoryImpl.registerUser(currentState.email, currentState.password)
                result.fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                        onSuccess()
                    },
                    onFailure = { error ->
                        val errorMessage = error.message ?: "Falha no registro"
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = errorMessage
                            )
                        }
                        onError(errorMessage)
                    }
                )
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Erro inesperado"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = errorMessage
                    )
                }
                onError(errorMessage)
            }
        } else {
            onError("Há campos inválidos")
            _uiState.update {
                it.copy(errorMessage = "Há campos inválidos")
            }
        }
    }
}