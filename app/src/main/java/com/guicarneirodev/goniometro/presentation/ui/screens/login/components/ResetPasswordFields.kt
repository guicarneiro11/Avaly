package com.guicarneirodev.goniometro.presentation.ui.screens.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import com.guicarneirodev.goniometro.ui.theme.SuccessGreen

@Composable
fun ResetPasswordFields(
    email: String,
    onEmailChange: (String) -> Unit,
    securityCode: String,
    onSecurityCodeChange: (String) -> Unit,
    resetCodeSent: Boolean,
    resetEmailSent: Boolean,
    isLoading: Boolean,
    onSendResetCodeClick: () -> Unit,
    onVerifyResetCodeClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(
            visible = !resetCodeSent,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text(stringResource(R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reset_email_field"),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        focusedLabelColor = AccentBlue,
                        cursorColor = AccentBlue,
                        unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                        unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f),
                        disabledBorderColor = SecondaryDark.copy(alpha = 0.3f),
                        disabledLabelColor = SecondaryDark.copy(alpha = 0.5f)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = onSendResetCodeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("send_code_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        disabledContainerColor = AccentBlue.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = PrimaryLight,
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("loading_indicator")
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.send_code),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryLight
                        )
                    }
                }

                if (resetEmailSent) {
                    Text(
                        text = stringResource(R.string.recovery_email_sent),
                        color = SuccessGreen,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = resetCodeSent,
            enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
            exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = securityCode,
                    onValueChange = onSecurityCodeChange,
                    label = { Text(stringResource(R.string.security_code)) },
                    modifier = Modifier.fillMaxWidth().testTag("reset_code_field"),
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentBlue,
                        focusedLabelColor = AccentBlue,
                        cursorColor = AccentBlue,
                        unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                        unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f),
                        disabledBorderColor = SecondaryDark.copy(alpha = 0.3f),
                        disabledLabelColor = SecondaryDark.copy(alpha = 0.5f)
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = onVerifyResetCodeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("verify_code_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentBlue,
                        disabledContainerColor = AccentBlue.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = PrimaryLight,
                            modifier = Modifier
                                .size(24.dp)
                                .testTag("loading_indicator")
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.verify_code),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryLight
                        )
                    }
                }
            }
        }
    }
}