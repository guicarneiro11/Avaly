package com.guicarneirodev.goniometro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.guicarneirodev.goniometro.R
import com.guicarneirodev.goniometro.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

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
    label: String
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
                .padding(8.dp),
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

@Composable
fun Register(navController: NavController, viewModel: RegisterViewModel) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    var passwordVisibility by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    RegisterScreen(
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        emailError = emailError,
        passwordError = passwordError,
        confirmPasswordError = confirmPasswordError,
        passwordVisibility = passwordVisibility,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onConfirmPasswordChange = viewModel::updateConfirmPassword,
        onPasswordVisibilityChange = { passwordVisibility = it },
        onRegisterClick = {
            viewModel.registerUser(
                onSuccess = { navController.navigate("home") },
                onError = { error -> errorMessage = error }
            )
        },
        onBackClick = { navController.popBackStack() },
        errorMessage = errorMessage
    )
}

@Composable
fun RegisterScreen(
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
    onBackClick: () -> Unit,
    errorMessage: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2C73B1), Color(0xFF2C73B1))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                RegisterForm(
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError,
                    passwordVisibility = passwordVisibility,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onPasswordVisibilityChange = onPasswordVisibilityChange,
                    onRegisterClick = onRegisterClick,
                    onBackClick = onBackClick
                )
            }
            if (errorMessage.isNotEmpty()) {
                item {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

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
            label = "Senha"
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            password = confirmPassword,
            error = confirmPasswordError,
            onValueChange = onConfirmPasswordChange,
            passwordVisibility = passwordVisibility,
            onPasswordVisibilityChange = onPasswordVisibilityChange,
            label = "Confirmar senha"
        )
        Spacer(modifier = Modifier.height(16.dp))
        RegisterButton(
            onClick = onRegisterClick,
            enabled = email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
                    emailError.isEmpty() && passwordError.isEmpty() && confirmPasswordError.isEmpty()
        )
    }
}