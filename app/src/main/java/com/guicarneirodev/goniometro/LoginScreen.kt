package com.guicarneirodev.goniometro

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun Login(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToMain) {
        if (uiState.navigateToMain) {
            navController.navigate("main")
        }
    }
    LoginScreen(
        viewModel = viewModel
    )
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

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
                LoginContent(
                    uiState = uiState,
                    onEmailChange = viewModel::onEmailChange,
                    onPasswordChange = viewModel::onPasswordChange,
                    onRememberEmailChange = viewModel::onRememberEmailChange,
                    onRememberPasswordChange = viewModel::onRememberPasswordChange,
                    onLoginClick = viewModel::onLoginClick,
                    onResetPasswordClick = viewModel::onResetPasswordClick,
                    onBackToLoginClick = viewModel::onBackToLoginClick,
                    onSendResetCodeClick = viewModel::onSendResetCodeClick,
                    onVerifyResetCodeClick = viewModel::onVerifyResetCodeClick,
                    onSecurityCodeChange = viewModel::onSecurityCodeChange
                )
            }
        }
    }
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberEmailChange: (Boolean) -> Unit,
    onRememberPasswordChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onResetPasswordClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    onSendResetCodeClick: () -> Unit,
    onVerifyResetCodeClick: () -> Unit,
    onSecurityCodeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.showResetPassword) {
                ResetPasswordContent(
                    email = uiState.email,
                    onEmailChange = onEmailChange,
                    securityCode = uiState.securityCode,
                    onSecurityCodeChange = onSecurityCodeChange,
                    resetCodeSent = uiState.resetCodeSent,
                    onSendResetCodeClick = onSendResetCodeClick,
                    onVerifyResetCodeClick = onVerifyResetCodeClick,
                    onBackToLoginClick = onBackToLoginClick
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
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

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
    val visibilityIcon = if (passwordVisibility) R.drawable.pass_on else R.drawable.pass_off

    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    TextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Senha") },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(
                    painter = painterResource(id = visibilityIcon),
                    contentDescription = if (passwordVisibility) "Esconder Senha" else "Mostrar Senha"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = rememberEmail,
            onCheckedChange = onRememberEmailChange
        )
        Text("Lembrar Email")

        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = rememberPassword,
            onCheckedChange = onRememberPasswordChange
        )
        Text("Lembrar Senha")
    }

    Button(
        onClick = onLoginClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text("Entrar")
    }

    Text(
        text = "Esqueceu sua senha?",
        color = Color.Blue,
        modifier = Modifier
            .clickable(onClick = onResetPasswordClick)
            .padding(vertical = 8.dp)
    )
}

@Composable
fun ResetPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    securityCode: String,
    onSecurityCodeChange: (String) -> Unit,
    resetCodeSent: Boolean,
    onSendResetCodeClick: () -> Unit,
    onVerifyResetCodeClick: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    if (resetCodeSent) {
        TextField(
            value = securityCode,
            onValueChange = onSecurityCodeChange,
            label = { Text("Código de Segurança") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = onVerifyResetCodeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Verificar Código")
        }
    } else {
        TextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = onSendResetCodeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Enviar Código de Segurança")
        }
    }

    Text(
        text = "Voltar para o login",
        color = Color.Blue,
        modifier = Modifier
            .clickable(onClick = onBackToLoginClick)
            .padding(vertical = 8.dp)
    )
}