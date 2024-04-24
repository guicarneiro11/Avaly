package com.guicarneirodev.goniometro

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class FirebaseAuthManager {
    fun resetPassword(email: String, newPassword: String): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return "Usuário não encontrado"

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Endereço de e-mail inválido"
        }
        firebaseUser.updatePassword(newPassword)
            .addOnCompleteListener { resetPassword ->
                if (resetPassword.isSuccessful) {
                    println("Senha alterada com sucesso")
                } else {
                    val exception = resetPassword.exception
                    if (exception is FirebaseAuthInvalidCredentialsException) {
                        println("Senha inválida")
                    } else {
                        println("Erro desconhecido: ${exception?.message}")
                    }
                }
            }
        return "Senha alterada com sucesso"
    }

    private val firebaseAuth = FirebaseAuth.getInstance()
    fun signInEmail(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Login bem-sucedido: $email")
                    callback(Result.success(Unit))
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException ||
                        exception is FirebaseAuthInvalidCredentialsException
                ) {
                    println("Falha no login: Email ou senha incorretos")
                    callback(Result.failure(Exception("Email ou senha incorretos")))
                } else {
                    println("Falha no login: ${exception?.message}")
                    callback(Result.failure(Exception(exception?.message)))
                }
            }
        }
    }
}

@Composable
fun Login(navController: NavController) {
    val sharedPreferences = LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE)
    var email by remember { mutableStateOf(sharedPreferences.getString("email", "") ?: "") }
    var password by remember { mutableStateOf(sharedPreferences.getString("senha", "") ?: "") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordsMatch by remember { mutableStateOf(false) }
    var lembrarEmail by remember { mutableStateOf(sharedPreferences.getBoolean("lembrarEmail", false)) }
    var lembrarSenha by remember { mutableStateOf(sharedPreferences.getBoolean("lembrarSenha", false)) }

    var loginError by remember { mutableStateOf(false) }
    var showResetPassword by remember { mutableStateOf(false) }

    val firebaseAuthManager = FirebaseAuthManager()
    val context = LocalContext.current

    fun saveEmailNoSharedPreferences(context: Context, email: String) {
        val prefs = context.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun removerEmailDoSharedPreferences(context: Context) {
        val prefs = context.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("email")
        editor.apply()
    }

    fun saveSenhaNoSharedPreferences(context: Context, senha: String) {
        val prefs = context.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("senha", senha)
        editor.apply()
    }

    fun removerSenhaDoSharedPreferences(context: Context) {
        val prefs = context.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("senha")
        editor.apply()
    }

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
                        if (showResetPassword) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrowback),
                                    contentDescription = "Voltar Tela",
                                    modifier = Modifier
                                        .clickable { showResetPassword = false }
                                        .size(40.dp)
                                )

                                Spacer(modifier = Modifier.weight(0.9f))

                                Text(
                                    text = "Alterar Senha",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.SansSerif),
                                    modifier = Modifier.padding(16.dp),
                                )

                                Spacer(modifier = Modifier.weight(1.5f))

                            }
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                            TextField(value = currentPassword, onValueChange = {
                                currentPassword = it },
                                label = { Text("Senha atual") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                visualTransformation = PasswordVisualTransformation()
                            )
                            TextField(
                                value = newPassword,
                                onValueChange = {newPassword = it },
                                label = { Text("Nova senha") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                visualTransformation = PasswordVisualTransformation()
                            )
                            TextField(
                                value = confirmPassword,
                                onValueChange = {
                                    confirmPassword = it
                                    passwordsMatch = it == newPassword},
                                label = { Text("Confirmar nova senha") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                visualTransformation = PasswordVisualTransformation()
                            )

                            Button(onClick = { firebaseAuthManager.resetPassword(email, newPassword); navController.popBackStack()},
                                enabled = passwordsMatch) {
                                Text("Redefinir senha")
                            }

                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.arrowback),
                                    contentDescription = "Voltar Tela",
                                    modifier = Modifier
                                        .clickable { navController.popBackStack() }
                                        .size(40.dp)
                                )

                                Spacer(modifier = Modifier.weight(0.9f))

                                Text(
                                    text = "Faça o login",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.SansSerif),
                                    modifier = Modifier.padding(16.dp),
                                )

                                Spacer(modifier = Modifier.weight(1.5f))

                            }
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Switch(checked = lembrarEmail, onCheckedChange = {
                                    lembrarEmail = it
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean("lembrarEmail", it)
                                    editor.apply()
                                }
                                ,modifier = Modifier.padding(start = 16.dp)
                                )
                                Text(
                                    text = "Lembrar email",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Senha") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                visualTransformation = PasswordVisualTransformation()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(checked = lembrarSenha, onCheckedChange = {
                                    lembrarSenha = it
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean("lembrarSenha", it)
                                    editor.apply()
                                }
                                ,modifier = Modifier.padding(start = 16.dp)
                                    )
                                Text(
                                    text = "Lembrar senha",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Button(
                                onClick = {
                                    if (email.isNotEmpty() && password.isNotEmpty()) {
                                        if (lembrarEmail) {
                                            saveEmailNoSharedPreferences(context, email)
                                        } else {
                                            removerEmailDoSharedPreferences(context)
                                        }
                                        if (lembrarSenha) {
                                            saveSenhaNoSharedPreferences(context, password)
                                        } else {
                                            removerSenhaDoSharedPreferences(context)
                                        }
                                        firebaseAuthManager.signInEmail(email, password) { result ->
                                            result.fold(
                                                onSuccess = {
                                                    navController.navigate("main")
                                                },
                                                onFailure = { loginError = true }
                                            )
                                        }
                                    } else {
                                        loginError = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Entrar",
                                    color = Color(0xFFFFFFFF),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.Default),
                                )
                            }

                            if (loginError) {
                                Text(
                                    text = "Email ou senha incorretos. Por favor, tente novamente.",
                                    color = Color.Red
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Esqueceu sua senha?",
                                color = Color.Blue,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.clickable { showResetPassword = true }
                            )
                        }
                    }
                }
            }
        }
    }
}