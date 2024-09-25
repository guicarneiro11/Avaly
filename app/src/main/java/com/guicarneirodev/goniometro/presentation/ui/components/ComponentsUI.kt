package com.guicarneirodev.goniometro.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guicarneirodev.goniometro.R

@Composable
fun BackButton(onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = R.drawable.voltar),
        contentDescription = "Voltar Tela",
        modifier = Modifier
            .clickable(onClick = onClick)
            .size(40.dp),
        tint = Color(0xFF000000)
    )
}

@Composable
fun EmailField(
    email: String,
    error: String,
    onValueChange: (String) -> Unit
) {
    Column {
        TextField(
            value = email,
            onValueChange = onValueChange,
            label = {
                Text(
                    "Email",
                    color = Color(0xFF0F0F0F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            isError = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun PasswordField(
    password: String,
    error: String,
    onValueChange: (String) -> Unit,
    passwordVisibility: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    label: String,
    testTag: String
) {
    Column {
        TextField(
            value = password,
            onValueChange = onValueChange,
            label = {
                Text(
                    label,
                    color = Color(0xFF0F0F0F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag(testTag),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            isError = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibilityChange(!passwordVisibility) }) {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisibility) R.drawable.pass_on else R.drawable.pass_off
                        ),
                        contentDescription = if (passwordVisibility) "Esconder Senha" else "Mostrar Senha",
                        tint = Color(0xFF000000)
                    )
                }
            }
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun RegisterButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF3280C4),
            disabledContainerColor = Color(0xFFA1A1A1)
        ),
        enabled = enabled
    ) {
        Text(
            text = "Criar conta",
            color = Color(0xFFFFFFFF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif
        )
    }
}