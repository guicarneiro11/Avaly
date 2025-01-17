package com.guicarneirodev.goniometro.presentation.ui.screens.patients.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.presentation.viewmodel.UiState
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.ErrorRed
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import com.guicarneirodev.goniometro.ui.theme.SuccessGreen
import kotlinx.coroutines.delay

@Composable
fun SendPdfDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit,
    uiState: UiState
) {
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Loading -> {
                isLoading = true
                showSuccess = false
            }
            is UiState.Success -> {
                isLoading = false
                showSuccess = true
                delay(2000)
                onDismiss()
            }
            is UiState.Error -> {
                isLoading = false
                showSuccess = false
            }
            else -> {
                isLoading = false
                showSuccess = false
            }
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = PrimaryLight,
        title = {
            Text(
                stringResource(R.string.send_pdf),
                style = MaterialTheme.typography.headlineSmall,
                color = AccentBlue,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                    },
                    enabled = !isLoading && !showSuccess,
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidEmail && email.isNotEmpty(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        focusedLabelColor = AccentBlue,
                        unfocusedBorderColor = SecondaryDark.copy(alpha = 0.3f),
                        unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f),
                        errorBorderColor = ErrorRed,
                        errorLabelColor = ErrorRed
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = AccentBlue
                    )
                }

                if (showSuccess) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.pdf_sent_successfully),
                            color = SuccessGreen,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                when (uiState) {
                    is UiState.Error -> {
                        Text(
                            uiState.message,
                            color = ErrorRed,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    else -> {}
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (isValidEmail && email.isNotBlank()) onSend(email) },
                enabled = isValidEmail && email.isNotBlank() && !isLoading && !showSuccess,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue,
                    contentColor = PrimaryLight,
                    disabledContainerColor = AccentBlue.copy(alpha = 0.5f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = PrimaryLight,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(R.string.send))
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isLoading,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AccentBlue,
                    disabledContentColor = AccentBlue.copy(alpha = 0.5f)
                ),
                border = BorderStroke(1.dp, if (isLoading) AccentBlue.copy(alpha = 0.5f) else AccentBlue)
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}