package com.guicarneirodev.goniometro.presentation.ui.screens.register.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.viewmodel.RegisterUiState

@Composable
fun RegisterCard(
    uiState: RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
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
            // Email field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                isError = uiState.emailError.isNotEmpty(),
                supportingText = {
                    if (uiState.emailError.isNotEmpty()) {
                        Text(uiState.emailError)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            // Password field
            var passwordVisibility by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Senha") },
                isError = uiState.passwordError.isNotEmpty(),
                supportingText = {
                    if (uiState.passwordError.isNotEmpty()) {
                        Text(uiState.passwordError)
                    }
                },
                visualTransformation = if (passwordVisibility)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility },
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisibility)
                                    R.drawable.pass_on
                                else
                                    R.drawable.pass_off
                            ),
                            contentDescription = if (passwordVisibility)
                                "Esconder Senha"
                            else
                                "Mostrar Senha"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            // Confirm Password field
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirmar Senha") },
                isError = uiState.confirmPasswordError.isNotEmpty(),
                supportingText = {
                    if (uiState.confirmPasswordError.isNotEmpty()) {
                        Text(uiState.confirmPasswordError)
                    }
                },
                visualTransformation = if (passwordVisibility)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility },
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisibility)
                                    R.drawable.pass_on
                                else
                                    R.drawable.pass_off
                            ),
                            contentDescription = if (passwordVisibility)
                                "Esconder Senha"
                            else
                                "Mostrar Senha"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            // Error message
            if (uiState.errorMessage.isNotEmpty()) {
                Text(
                    text = uiState.errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            // Register button
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading &&
                        uiState.email.isNotEmpty() &&
                        uiState.password.isNotEmpty() &&
                        uiState.confirmPassword.isNotEmpty() &&
                        uiState.emailError.isEmpty() &&
                        uiState.passwordError.isEmpty() &&
                        uiState.confirmPasswordError.isEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5),
                    disabledContainerColor = Color(0xFF1E88E5).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Criar conta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}