package com.guicarneirodev.goniometro.presentation.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guicarneirodev.goniometro.presentation.viewmodel.RegisterScreenViewModel
import com.guicarneirodev.goniometro.presentation.ui.screens.home.components.BackgroundDecorations
import com.guicarneirodev.goniometro.presentation.ui.screens.register.components.RegisterForm
import com.guicarneirodev.goniometro.presentation.ui.screens.register.components.RegisterHeader

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterScreenViewModel) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E88E5),
                        Color(0xFF4FC3F7)
                    )
                )
            )
    ) {
        // Elementos decorativos de fundo
        BackgroundDecorations()

        // Conteúdo principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabeçalho com título e botão voltar
            RegisterHeader(
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Formulário
            RegisterForm(
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
                errorMessage = errorMessage
            )
        }
    }
}