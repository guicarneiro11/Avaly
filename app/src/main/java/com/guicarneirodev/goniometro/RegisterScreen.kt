package com.guicarneirodev.goniometro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Register(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
        passwordVisibility = passwordVisibility,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onConfirmPasswordChange = viewModel::updateConfirmPassword,
        onPasswordVisibilityChange = { passwordVisibility = it },
        onRegisterClick = {
            viewModel.registerUser(
                onSuccess = { navController.navigate("home") },
                onError = { error -> errorMessage = error }
            )
        },
        onBackClick = { navController.popBackStack() },
        errorMessage = errorMessage
    )
}