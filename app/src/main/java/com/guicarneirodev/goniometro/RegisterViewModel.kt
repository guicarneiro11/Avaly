package com.guicarneirodev.goniometro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val validator: Validator,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _emailError = MutableStateFlow("")
    val emailError: StateFlow<String> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow("")
    val passwordError: StateFlow<String> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow("")
    val confirmPasswordError: StateFlow<String> = _confirmPasswordError.asStateFlow()

    fun updateEmail(email: String) {
        _email.value = email
        validateEmail()
    }

    fun updatePassword(password: String) {
        _password.value = password
        validatePassword()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        validateConfirmPassword()
    }

    private fun validateEmail() {
        _emailError.value = if (validator.isEmailValid(_email.value)) "" else "Email inválido"
    }

    private fun validatePassword() {
        _passwordError.value = validator.getPasswordError(_password.value)
    }

    private fun validateConfirmPassword() {
        _confirmPasswordError.value = validator.passwordMatchError(_password.value, _confirmPassword.value)
    }

    fun registerUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (_emailError.value.isEmpty() && _passwordError.value.isEmpty() && _confirmPasswordError.value.isEmpty()) {
            viewModelScope.launch {
                authRepository.registerUser(_email.value, _password.value)
                    .onSuccess { onSuccess() }
                    .onFailure { onError(it.message ?: "Falha no registro") }
            }
        } else {
            onError("Há campos inválidos")
        }
    }
}