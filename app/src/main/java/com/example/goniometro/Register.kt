package com.example.goniometro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController) {
    var phone by remember { mutableStateOf("") }
    var isPhoneErrorVisible by remember { mutableStateOf(true) }
    val phoneFocusRequester = remember { FocusRequester() }
    val isPhoneValid = { phoneNumber: String -> phoneNumber.matches(Regex("\\d{11}")) }

    var username by remember { mutableStateOf("") }
    var usernameErrorVisible by remember { mutableStateOf(true) }
    val usernameFocusRequester = remember {FocusRequester () }
    val isUsernameValid = { inputUsername: String -> inputUsername.isNotEmpty() && inputUsername.length >= 4 }

    var password by remember { mutableStateOf("") }
    var passwordErrorVisible by remember { mutableStateOf(true) }
    val passwordFocusRequester = remember {FocusRequester () }
    val isPasswordValid = { inputPassword: String ->
        inputPassword.length >= 8 && inputPassword.contains(Regex("[A-Z]")) &&
                inputPassword.contains(Regex("[a-z]")) && inputPassword.contains(Regex("\\d"))
    }

    var showErrorText by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB2FFED),
                        Color(0xFFCEB3FF)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Voltar(navController)

                TextField(
                        value = phone,
                        onValueChange = { phone = it
                                        isPhoneErrorVisible = false
                                        },
                        label = { Text("Celular") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .focusRequester(phoneFocusRequester),
                    isError = isPhoneErrorVisible,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            isPhoneErrorVisible = !isPhoneValid(phone)
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                    )
                if (!isPhoneValid(phone)) {
                    Text(
                        text = "Número de telefone inválido",
                        color = Color.Red
                    )
                }
                DisposableEffect(Unit){
                    phoneFocusRequester.requestFocus()
                    onDispose{}
                }


                TextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameErrorVisible = false
                    },
                    label = { Text("Nome de usuário") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(usernameFocusRequester),
                    isError = usernameErrorVisible,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            usernameErrorVisible = !isUsernameValid(username)
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )
                if (!isUsernameValid(username)) {
                    Text(
                        text = "Nome de usuário inválido",
                        color = Color.Red
                    )
                }

                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordErrorVisible = false
                    },
                    label = { Text("Senha") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .focusRequester(passwordFocusRequester),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = passwordErrorVisible,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            passwordErrorVisible = !isPasswordValid(password)
                        }
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )
                if (!isPasswordValid(password)) {
                    Text(
                        text = "Senha inválida",
                        color = Color.Red
                    )
                }
                    Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (!isPhoneErrorVisible && !usernameErrorVisible && !passwordErrorVisible) {
                            println("Celular: $phone, Nome de usuário: $username, Senha: $password")
                            navController.navigate("inicio")
                        } else {
                            showErrorText = true
                            errorMessage = "Há campos inválidos"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Registrar-se")
                }
            }
        }
    }
}