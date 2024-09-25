package com.guicarneirodev.goniometro.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.guicarneirodev.goniometro.data.repository.FirebaseRepository
import com.guicarneirodev.goniometro.data.repository.LoginRepository
import com.guicarneirodev.goniometro.data.repository.LoginPreferencesRepository
import com.guicarneirodev.goniometro.data.repository.SharedPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel(
    private val authRepository: LoginRepository,
    private val loginPreferencesRepository: LoginPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = LoginUiState(
            email = loginPreferencesRepository.getEmail(),
            password = loginPreferencesRepository.getPassword(),
            rememberEmail = loginPreferencesRepository.getRememberEmail(),
            rememberPassword = loginPreferencesRepository.getRememberPassword()
        )
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onRememberEmailChange(remember: Boolean) {
        _uiState.value = _uiState.value.copy(rememberEmail = remember)
        loginPreferencesRepository.setRememberEmail(remember)
    }

    fun onRememberPasswordChange(remember: Boolean) {
        _uiState.value = _uiState.value.copy(rememberPassword = remember)
        loginPreferencesRepository.setRememberPassword(remember)
    }

    fun onSecurityCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(securityCode = code)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val result = authRepository.signInWithEmail(_uiState.value.email, _uiState.value.password)
            result.fold(
                onSuccess = {
                    saveCredentials()
                    _uiState.value = _uiState.value.copy(navigateToMain = true)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(errorMessage = "Email ou senha incorretos")
                }
            )
        }
    }

    fun onResetPasswordClick() {
        _uiState.value = _uiState.value.copy(showResetPassword = true)
    }

    fun onBackToLoginClick() {
        _uiState.value = _uiState.value.copy(showResetPassword = false)
    }

    fun onSendResetCodeClick() {
        viewModelScope.launch {
            val result = authRepository.sendSecurityCode(_uiState.value.email)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(resetCodeSent = true)
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(errorMessage = "Falha ao enviar código de redefinição")
                }
            )
        }
    }

    fun onVerifyResetCodeClick() {
        viewModelScope.launch {
            val result = authRepository.verifySecurityCode(_uiState.value.email, _uiState.value.securityCode)
            result.fold(
                onSuccess = { isValid ->
                    if (isValid) {
                        authRepository.resetPassword(_uiState.value.email)
                        _uiState.value = _uiState.value.copy(
                            showResetPassword = false,
                            resetCodeSent = false,
                            securityCode = "",
                            errorMessage = "Email de redefinição enviado com sucesso"
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(errorMessage = "Código de segurança inválido")
                    }
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(errorMessage = "Falha ao verificar código de segurança")
                }
            )
        }
    }

    private fun saveCredentials() {
        if (_uiState.value.rememberEmail) loginPreferencesRepository.setEmail(_uiState.value.email)
        else loginPreferencesRepository.setEmail("")
        if (_uiState.value.rememberPassword) loginPreferencesRepository.setPassword(_uiState.value.password)
        else loginPreferencesRepository.setPassword("")
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberEmail: Boolean = false,
    val rememberPassword: Boolean = false,
    val showResetPassword: Boolean = false,
    val resetCodeSent: Boolean = false,
    val securityCode: String = "",
    val errorMessage: String? = null,
    val navigateToMain: Boolean = false
)