package com.guicarneirodev.goniometro.presentation.ui.screens.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.ui.theme.AccentBlue
import com.guicarneirodev.goniometro.ui.theme.PrimaryLight
import com.guicarneirodev.goniometro.ui.theme.SecondaryDark
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginFields(
    email: String,
    password: String,
    rememberEmail: Boolean,
    rememberPassword: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberEmailChange: (Boolean) -> Unit,
    onRememberPasswordChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onResetPasswordClick: () -> Unit
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.testTag("login_fields_container")
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth().testTag("email_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                focusedLabelColor = AccentBlue,
                cursorColor = AccentBlue,
                unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth().testTag("password_field"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                focusedLabelColor = AccentBlue,
                cursorColor = AccentBlue,
                unfocusedBorderColor = SecondaryDark.copy(alpha = 0.5f),
                unfocusedLabelColor = SecondaryDark.copy(alpha = 0.7f)
            ),
            singleLine = true,
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisibility) R.drawable.pass_on
                            else R.drawable.pass_off
                        ),
                        contentDescription = if (passwordVisibility)
                            stringResource(R.string.hide_password)
                        else
                            stringResource(R.string.show_password),
                        tint = AccentBlue
                    )
                }
            },
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.testTag("remember_options_container")
            ) {
                Checkbox(
                    checked = rememberEmail,
                    onCheckedChange = onRememberEmailChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = AccentBlue,
                        uncheckedColor = SecondaryDark.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.testTag("remember_email_checkbox")
                )
                Text(
                    stringResource(R.string.remember_email),
                    fontSize = 14.sp,
                    color = SecondaryDark
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Checkbox(
                    checked = rememberPassword,
                    onCheckedChange = onRememberPasswordChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = AccentBlue,
                        uncheckedColor = SecondaryDark.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.testTag("remember_password_checkbox")
                )
                Text(
                    stringResource(R.string.remember_password),
                    fontSize = 14.sp,
                    color = SecondaryDark
                )
            }
        }

        var isClickable by remember { mutableStateOf(true) }

        Button(
            onClick = {
                if (isClickable) {
                    isClickable = false
                    onLoginClick()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        isClickable = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("login_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                disabledContainerColor = AccentBlue.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = isClickable
        ) {
            Text(
                text = stringResource(R.string.enter),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryLight
            )
        }

        Text(
            text = stringResource(R.string.forgot_password),
            color = AccentBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .testTag("forgot_password_button")
                .clickable(onClick = onResetPasswordClick)
                .padding(vertical = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}