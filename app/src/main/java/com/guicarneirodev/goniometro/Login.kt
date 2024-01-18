package com.guicarneirodev.goniometro

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavController) {
    val sharedPreferences = LocalContext.current.getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
    var email by remember { mutableStateOf(sharedPreferences.getString("email", "") ?: "") }
    var password by remember { mutableStateOf(sharedPreferences.getString("senha", "") ?: "") }
    var lembrarEmail by remember { mutableStateOf(sharedPreferences.getBoolean("lembrarEmail", false)) }
    var lembrarSenha by remember { mutableStateOf(sharedPreferences.getBoolean("lembrarSenha", false)) }
    var loginError by remember { mutableStateOf(false) }

    val firebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    fun saveEmailNoSharedPreferences(context: Context, email: String) {
        val prefs = context.getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun removerEmailDoSharedPreferences(context: Context) {
        val prefs = context.getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("email")
        editor.apply()
    }

    fun saveSenhaNoSharedPreferences(context: Context, senha: String) {
        val prefs = context.getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("senha", senha)
        editor.apply()
    }

    fun removerSenhaDoSharedPreferences(context: Context) {
        val prefs = context.getSharedPreferences("MinhasPreferencias", Context.MODE_PRIVATE)
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Voltar(navController)

                            Spacer(modifier = Modifier.weight(0.7f))

                            Text(
                                text = "Faça o login",
                                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily.SansSerif),
                                modifier = Modifier.padding(16.dp),
                            )
                            Spacer(modifier = Modifier.weight(2f))
                        }
                        TextField(
                            value = email,
                            onValueChange = {
                                email = it
                                val editor = sharedPreferences.edit()
                                editor.putString("email", it)
                                editor.apply()
                            },
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
                            Checkbox(checked = lembrarEmail, onCheckedChange = {
                                lembrarEmail = it
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("lembrarEmail", it)
                                editor.apply()
                            })
                            Text(
                                text = "Lembrar Email",
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
                            Checkbox(checked = lembrarSenha, onCheckedChange = {
                                lembrarSenha = it
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("lembrarSenha", it)
                                editor.apply()
                            })
                            Text(
                                text = "Lembrar Senha",
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

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

                                    firebaseAuth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                println("Login bem-sucedido: $email")
                                                navController.navigate("home")
                                            } else {
                                                val exception = task.exception
                                                if (exception is FirebaseAuthInvalidUserException ||
                                                    exception is FirebaseAuthInvalidCredentialsException
                                                ) {
                                                    println("Falha no login: Email ou senha incorretos")
                                                    loginError = true
                                                } else {
                                                    println("Falha no login: ${exception?.message}")
                                                }
                                            }
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
                    }
                }
            }
        }
    }
}