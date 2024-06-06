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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

            class ValidViewModel : ViewModel() {
                private val emailPattern: Pattern = Pattern.compile(
                    "[a-zA-Z0-9+._%\\-]{1,256}" + "@" +
                            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                            "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
                )

                private val passwordPattern: Pattern = Pattern.compile(
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+_(){}|~/<>.?,€£¥=])(?=\\S+$).{6,}$"
                )

                fun passwordsMatch(password: String, confirmPassword: String): Boolean {
                    return password == confirmPassword
                }

                fun passwordMatchError(password: String, confirmPassword: String): String {
                    if (password != confirmPassword) {
                        return "As senhas não coincidem."
                    }
                    return ""
                }

                fun isEmailValid(email: String): Boolean {
                    return emailPattern.matcher(email).matches()
                }

                fun isPasswordValid(password: String): Boolean {
                    return passwordPattern.matcher(password).matches()
                }

                fun getPasswordError(password: String): String {
                    if (password.length < 6) {
                        return "A senha deve ter pelo menos 6 caracteres."
                    }
                    if (!password.any { it.isLowerCase() }) {
                        return "A senha deve conter pelo menos uma letra minúscula."
                    }
                    if (!password.any { it.isUpperCase() }) {
                        return "A senha deve conter pelo menos uma letra maiúscula."
                    }
                    if (!password.any { it.isDigit() }) {
                        return "A senha deve conter pelo menos um número."
                    }
                    if (!password.contains(Regex("[!@#$%^&+_(){}|~/<>.?,€£¥=]"))) {
                        return "A senha deve conter pelo menos um caractere especial."
                    }
                    return ""
                }
            }
            @Composable
            fun Register(navController: NavController, viewModel: ValidViewModel) {
                val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

                var email by remember { mutableStateOf("") }
                var isEmailErrorVisible by remember { mutableStateOf(true) }
                val emailFocusRequester = remember { FocusRequester() }
                val isEmailValid = { input: String -> android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches() }

                var password by remember { mutableStateOf("") }
                var passwordErrorVisible by remember { mutableStateOf(true) }
                val passwordFocusRequester = remember { FocusRequester() }
                val isPasswordValid = { input: String -> viewModel.isPasswordValid(input) }
                var confirmPassword by remember { mutableStateOf("") }
                var passwordVisibility by remember { mutableStateOf(false) }
                var passwordErrorMessage by remember { mutableStateOf("") }
                var passwordMatchError by remember { mutableStateOf("") }

                var errorMessage by remember { mutableStateOf("") }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF2C73B1),
                                    Color(0xFF2C73B1)
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
                                    Icon(
                                        painter = painterResource(id = R.drawable.voltar),
                                        contentDescription = "Voltar Tela",
                                        modifier = Modifier
                                            .clickable { navController.popBackStack() }
                                            .size(40.dp)
                                    )
                                    TextField(
                                        value = email,
                                        onValueChange = {
                                            email = it
                                            isEmailErrorVisible = false
                                        },
                                        label = { Text("Email",
                                            color = Color(0xFF0F0F0F),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = FontFamily.SansSerif) },
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
                                            passwordErrorMessage = viewModel.getPasswordError(password)
                                        },
                                        label = { Text("Senha",
                                            color = Color(0xFF0F0F0F),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = FontFamily.SansSerif) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .focusRequester(passwordFocusRequester),
                                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                        isError = passwordErrorMessage.isNotEmpty(),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                passwordErrorVisible = !isPasswordValid(password)
                                                passwordErrorMessage = viewModel.getPasswordError(password)
                                            }
                                        ),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Done
                                        ),
                                        trailingIcon = {
                                            IconButton(onClick = { passwordVisibility = !passwordVisibility}) {
                                                Icon (
                                                    painter  = if (passwordVisibility) painterResource(id = R.drawable.pass_off) else painterResource(id = R.drawable.pass_on),
                                                    contentDescription = if (passwordVisibility) "Esconder Senha" else "Mostrar Senha"
                                                )
                                            }
                                        }
                                    )
                                    if (passwordErrorMessage.isNotEmpty()) {
                                        Text(
                                            text = passwordErrorMessage,
                                            color = Color.Red
                                        )
                                    }
                                    TextField(
                                        value = confirmPassword,
                                        onValueChange = {
                                            confirmPassword = it
                                            println("Confirmar senha: $confirmPassword")
                                            passwordMatchError = viewModel.passwordMatchError(password, confirmPassword)
                                        },
                                        label = { Text("Confirmar senha",
                                            color = Color(0xFF0F0F0F),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            fontFamily = FontFamily.SansSerif) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .focusRequester(passwordFocusRequester),
                                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                        isError = password.isNotEmpty() && !isPasswordValid(password),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                passwordErrorVisible = !isPasswordValid(password)
                                                passwordMatchError = viewModel.passwordMatchError(password, confirmPassword)
                                            }
                                        ),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Done
                                        ),
                                        trailingIcon = {
                                            IconButton(onClick = { passwordVisibility = !passwordVisibility}) {
                                                Icon (
                                                    painter  = if (passwordVisibility) painterResource(id = R.drawable.pass_off) else painterResource(id = R.drawable.pass_on),
                                                    contentDescription = if (passwordVisibility) "Esconder Senha" else "Mostrar Senha"
                                                )
                                            }
                                        }
                                    )
                                    if (passwordMatchError.isNotEmpty()) {
                                        Text(
                                            text = passwordMatchError,
                                            color = Color.Red
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            if (viewModel.isEmailValid(email) &&
                                                viewModel.isPasswordValid(password) &&
                                                viewModel.passwordsMatch(password, confirmPassword)
                                            ) {
                                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener { authResult ->
                                                        if (authResult.isSuccessful) {
                                                            val firebaseUser = firebaseAuth.currentUser!!
                                                            println("Usuário autenticado: ${firebaseUser.email}")
                                                            navController.navigate("home")
                                                        } else {
                                                            errorMessage = authResult.exception?.message ?: "Falha no registro"
                                                        }
                                                    }
                                            } else {
                                                errorMessage = "Há campos inválidos"
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3280C4), disabledContainerColor = Color(0xFFA1A1A1)),
                                        enabled = viewModel.isEmailValid(email) &&
                                                viewModel.isPasswordValid(password) &&
                                                viewModel.passwordsMatch(password, confirmPassword)
                                    ) {
                                        Text(text = "Criar conta",
                                            color = Color(0xFFFFFFFF),
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.SansSerif)
                                    }
                                }
                            }
                        }
                    }
                }
            }