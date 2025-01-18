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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.viewmodel.RegisterUiState
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.ErrorRed
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark

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
            containerColor = PrimaryLight.copy(alpha = 0.95f)
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
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(R.string.email)) },
                isError = uiState.emailError.isNotEmpty(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentBlue,
                    focusedLabelColor = AccentBlue,
                    unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                    unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
                ),
                supportingText = {
                    if (uiState.emailError.isNotEmpty()) {
                        Text(uiState.emailError, color = ErrorRed, modifier = Modifier.testTag("email_error"))
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("email_field"),
                enabled = !uiState.isLoading
            )

            var passwordVisibility by remember { mutableStateOf(false) }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentBlue,
                    focusedLabelColor = AccentBlue,
                    unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                    unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
                ),
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(R.string.password)) },
                isError = uiState.passwordError.isNotEmpty(),
                supportingText = {
                    if (uiState.passwordError.isNotEmpty()) {
                        Text(uiState.passwordError, color = ErrorRed)
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
                                stringResource(R.string.hide_password)
                            else
                                stringResource(R.string.show_password)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("password_field"),
                enabled = !uiState.isLoading
            )

            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentBlue,
                    focusedLabelColor = AccentBlue,
                    unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                    unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
                ),
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text( stringResource(R.string.confirm_password)) },
                isError = uiState.confirmPasswordError.isNotEmpty(),
                supportingText = {
                    if (uiState.confirmPasswordError.isNotEmpty()) {
                        Text(uiState.confirmPasswordError, color = ErrorRed)
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
                                stringResource(R.string.hide_password)
                            else
                                stringResource(R.string.show_password)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().testTag("confirm_password_field"),
                enabled = !uiState.isLoading
            )

            if (uiState.errorMessage.isNotEmpty()) {
                Text(
                    text = uiState.errorMessage,
                    color = ErrorRed,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("register_button"),
                enabled = !uiState.isLoading &&
                        uiState.email.isNotEmpty() &&
                        uiState.password.isNotEmpty() &&
                        uiState.confirmPassword.isNotEmpty() &&
                        uiState.emailError.isEmpty() &&
                        uiState.passwordError.isEmpty() &&
                        uiState.confirmPasswordError.isEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    disabledContainerColor = AccentBlue.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    stringResource(R.string.create_account_button),
                    color = PrimaryLight,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}