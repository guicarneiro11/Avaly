package com.guicarneirodev.goniometro.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guicarneirodev.goniometro.domain.repository.LoginRepository
import com.guicarneirodev.goniometro.domain.repository.LoginPreferencesRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberEmail: Boolean = false,
    val rememberPassword: Boolean = false,
    val showResetPassword: Boolean = false,
    val resetCodeSent: Boolean = false,
    val securityCode: String = "",
    val errorMessage: String? = null,
    val navigateToSelection: Boolean = false,
    val isLoading: Boolean = false,
    val resetEmailSent: Boolean = false
)

open class LoginScreenViewModel(
    private val authRepository: LoginRepository,
    private val loginPreferencesRepository: LoginPreferencesRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _resetCodeActions = MutableSharedFlow<Unit>()
    private val _verifyCodeActions = MutableSharedFlow<Unit>()
    private val _loginActions = MutableSharedFlow<Unit>()

    init {
        _uiState.value = LoginUiState(
            email = loginPreferencesRepository.getEmail(),
            password = loginPreferencesRepository.getPassword(),
            rememberEmail = loginPreferencesRepository.getRememberEmail(),
            rememberPassword = loginPreferencesRepository.getRememberPassword()
        )

        setupActionHandlers()
    }

    @OptIn(FlowPreview::class)
    private fun setupActionHandlers() {
        viewModelScope.launch {
            _resetCodeActions
                .debounce(300)
                .collect { handleSendResetCode() }
        }

        viewModelScope.launch {
            _verifyCodeActions
                .debounce(300)
                .collect { handleVerifyResetCode() }
        }

        viewModelScope.launch {
            _loginActions
                .debounce(300)
                .collect { handleLogin() }
        }
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
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val result = authRepository.signInWithEmail(
                    _uiState.value.email,
                    _uiState.value.password
                )
                result.fold(
                    onSuccess = {
                        saveCredentials()
                        _uiState.update {
                            it.copy(
                                navigateToSelection = true,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update {
                            it.copy(
                                errorMessage = "Email ou senha incorretos",
                                isLoading = false
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Erro ao fazer login: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private suspend fun handleLogin() {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        try {
            val result =
                authRepository.signInWithEmail(_uiState.value.email, _uiState.value.password)
            result.fold(
                onSuccess = {
                    saveCredentials()
                    _uiState.value = _uiState.value.copy(
                        navigateToSelection = true,
                        isLoading = false
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Email ou senha incorretos",
                        isLoading = false
                    )
                }
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Erro ao fazer login: ${e.message}",
                isLoading = false
            )
        }
    }

    fun onResetPasswordClick() {
        _uiState.value = _uiState.value.copy(
            showResetPassword = true,
            resetCodeSent = false,
            resetEmailSent = false,
            errorMessage = null,
            securityCode = ""
        )
    }

    fun onBackToLoginClick() {
        _uiState.value = _uiState.value.copy(
            showResetPassword = false,
            resetCodeSent = false,
            resetEmailSent = false,
            errorMessage = null,
            securityCode = ""
        )
    }

    fun onSendResetCodeClick() {
        viewModelScope.launch {
            _resetCodeActions.emit(Unit)
        }
    }

    private suspend fun handleSendResetCode() {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        try {
            val result = authRepository.sendSecurityCode(_uiState.value.email)
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        resetCodeSent = true,
                        resetEmailSent = true,
                        isLoading = false,
                        errorMessage = null
                    )
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Falha ao enviar código de redefinição",
                        isLoading = false
                    )
                }
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Erro ao enviar código: ${e.message}",
                isLoading = false
            )
        }
    }

    fun onVerifyResetCodeClick() {
        viewModelScope.launch {
            _verifyCodeActions.emit(Unit)
        }
    }

    private suspend fun handleVerifyResetCode() {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        try {
            val result =
                authRepository.verifySecurityCode(_uiState.value.email, _uiState.value.securityCode)
            result.fold(
                onSuccess = { isValid ->
                    if (isValid) {
                        authRepository.resetPassword(_uiState.value.email)
                        _uiState.value = _uiState.value.copy(
                            showResetPassword = false,
                            resetCodeSent = false,
                            resetEmailSent = false,
                            securityCode = "",
                            errorMessage = "Email de redefinição enviado com sucesso",
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Código de segurança inválido",
                            isLoading = false
                        )
                    }
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Falha ao verificar código de segurança",
                        isLoading = false
                    )
                }
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Erro ao verificar código: ${e.message}",
                isLoading = false
            )
        }
    }

    private fun saveCredentials() {
        if (_uiState.value.rememberEmail) {
            loginPreferencesRepository.setEmail(_uiState.value.email)
        } else {
            loginPreferencesRepository.setEmail("")
        }

        if (_uiState.value.rememberPassword) {
            loginPreferencesRepository.setPassword(_uiState.value.password)
        } else {
            loginPreferencesRepository.setPassword("")
        }
    }
}