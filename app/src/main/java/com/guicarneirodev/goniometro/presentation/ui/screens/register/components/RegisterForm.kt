package com.guicarneirodev.goniometro.presentation.ui.screens.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    errorMessage: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EmailField(
                email = email,
                error = emailError,
                onValueChange = onEmailChange
            )

            PasswordField(
                password = password,
                error = passwordError,
                onValueChange = onPasswordChange,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                label = "Senha"
            )

            PasswordField(
                password = confirmPassword,
                error = confirmPasswordError,
                onValueChange = onConfirmPasswordChange,
                passwordVisibility = passwordVisibility,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                label = "Confirmar senha"
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            RegisterButton(
                onClick = onRegisterClick,
                enabled = email.isNotEmpty() &&
                        password.isNotEmpty() &&
                        confirmPassword.isNotEmpty() &&
                        emailError.isEmpty() &&
                        passwordError.isEmpty() &&
                        confirmPasswordError.isEmpty()
            )
        }
    }
}