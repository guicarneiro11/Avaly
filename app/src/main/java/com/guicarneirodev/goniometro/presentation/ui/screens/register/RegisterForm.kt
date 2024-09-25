package com.guicarneirodev.goniometro.presentation.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.presentation.ui.components.BackButton
import com.guicarneirodev.goniometro.presentation.ui.components.EmailField
import com.guicarneirodev.goniometro.presentation.ui.components.PasswordField

@Composable
fun RegisterForm(
    email: String,
    password: String,
    confirmPassword: String,
    emailError: String,
    passwordError: String,
    confirmPasswordError: String,
    passwordVisibility: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(onClick = onBackClick)
        Spacer(modifier = Modifier.height(16.dp))
        EmailField(email = email, error = emailError, onValueChange = onEmailChange)
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            password = password,
            error = passwordError,
            onValueChange = onPasswordChange,
            passwordVisibility = passwordVisibility,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            label = "Senha",
            testTag = "passwordField"
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            password = confirmPassword,
            error = confirmPasswordError,
            onValueChange = onConfirmPasswordChange,
            passwordVisibility = passwordVisibility,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            label = "Confirmar senha",
            testTag = "confirmPasswordField"
        )
        Spacer(modifier = Modifier.height(16.dp))
        com.guicarneirodev.goniometro.presentation.ui.components.RegisterButton(
            onClick = onRegisterClick,
            enabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                    emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()
        )
    }
}