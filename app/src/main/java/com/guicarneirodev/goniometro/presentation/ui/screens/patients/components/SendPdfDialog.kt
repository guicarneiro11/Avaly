package com.guicarneirodev.goniometro.presentation.ui.screens.patients.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.guicarneirodev.goniometro.R

@Composable
fun SendPdfDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title = {
            Text(
                stringResource(R.string.send_pdf),
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF1E88E5),
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
                    label = { Text (stringResource(R.string.email) ) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidEmail && email.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1E88E5),
                        focusedLabelColor = Color(0xFF1E88E5),
                        errorBorderColor = Color(0xFFE57373)
                    )
                )
                if (!isValidEmail && email.isNotEmpty()) {
                    Text(
                        stringResource(R.string.invalid_email),
                        color = Color(0xFFE57373),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (isValidEmail && email.isNotBlank()) onSend(email) },
                enabled = isValidEmail && email.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5),
                    disabledContainerColor = Color(0xFF1E88E5).copy(alpha = 0.5f)
                )
            ) {
                Text( stringResource(R.string.send) )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1E88E5)
                ),
                border = BorderStroke(1.dp, Color(0xFF1E88E5))
            ) {
                Text( stringResource(R.string.cancel) )
            }
        }
    )
}