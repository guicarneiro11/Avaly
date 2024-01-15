package com.guicarneirodev.goniometro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailErrorVisible by remember { mutableStateOf(true) }
    val emailFocusRequester = remember { FocusRequester() }
    val isEmailValid = { input: String -> android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches() }

    var password by remember { mutableStateOf("") }
    var passwordErrorVisible by remember { mutableStateOf(true) }
    val passwordFocusRequester = remember { FocusRequester() }
    val isPasswordValid = { input: String ->
        val isValid =
                input.length >= 6 &&
                input.contains(Regex("[A-Z]")) &&
                input.contains(Regex("[a-z]")) &&
                input.contains(Regex("\\d")) &&
                input.contains(Regex("[!@#$%^&*(),.?\":{}|<>~]"))

        println("Senha: $input, Válida: $isValid")

        isValid
    }

    var showErrorText by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF006F6A),
                        Color(0xFF006F6A)
                    )
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .background(Color.White, shape = RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Voltar(navController)

                        TextField(
                            value = email,
                            onValueChange = {
                                email = it
                                isEmailErrorVisible = false
                            },
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .focusRequester(emailFocusRequester),
                            isError = email.isNotEmpty() && !isEmailValid(email),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    isEmailErrorVisible = !isEmailValid(email)
                                }
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            )
                        )

                        TextField(
                            value = password,
                            onValueChange = {
                                password = it
                                println("Senha: $password")
                                passwordErrorVisible = false
                            },
                            label = { Text("Senha") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .focusRequester(passwordFocusRequester),
                            visualTransformation = PasswordVisualTransformation(),
                            isError = password.isNotEmpty() && !isPasswordValid(password),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    passwordErrorVisible = !isPasswordValid(password)
                                }
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (isEmailValid(email) && isPasswordValid(password)) {
                                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                val firebaseUser = firebaseAuth.currentUser!!
                                                println("Usuário autenticado: ${firebaseUser.email}")
                                                navController.navigate("inicio")
                                            } else {
                                                showErrorText = true
                                                errorMessage = "Falha no registro"
                                            }
                                        }
                                } else {
                                    showErrorText = true
                                    errorMessage = "Há campos inválidos"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(
                                text = "Criar conta",
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.Default)
                            )
                        }

                        if (showErrorText) {
                            Text(
                                text = errorMessage,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}