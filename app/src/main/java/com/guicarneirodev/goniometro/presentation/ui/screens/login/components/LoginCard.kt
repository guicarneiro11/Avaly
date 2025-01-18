package com.guicarneirodev.goniometro.presentation.ui.screens.login.components

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginUiState
import com.guicarneirodev.goniometro.ui.theme.ErrorRed
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight

@Composable
fun LoginCard(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberEmailChange: (Boolean) -> Unit,
    onRememberPasswordChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onResetPasswordClick: () -> Unit,
    onSendResetCodeClick: () -> Unit,
    onVerifyResetCodeClick: () -> Unit,
    onSecurityCodeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("login_card"),
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
            if (uiState.showResetPassword) {
                ResetPasswordFields(
                    email = uiState.email,
                    onEmailChange = onEmailChange,
                    securityCode = uiState.securityCode,
                    onSecurityCodeChange = onSecurityCodeChange,
                    resetCodeSent = uiState.resetCodeSent,
                    onSendResetCodeClick = onSendResetCodeClick,
                    onVerifyResetCodeClick = onVerifyResetCodeClick,
                    resetEmailSent = uiState.resetEmailSent,
                    isLoading = uiState.isLoading
                )
            } else {
                LoginFields(
                    email = uiState.email,
                    password = uiState.password,
                    rememberEmail = uiState.rememberEmail,
                    rememberPassword = uiState.rememberPassword,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onRememberEmailChange = onRememberEmailChange,
                    onRememberPasswordChange = onRememberPasswordChange,
                    onLoginClick = onLoginClick,
                    onResetPasswordClick = onResetPasswordClick
                )
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = ErrorRed,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("error_message")
                )
            }
        }
    }
}