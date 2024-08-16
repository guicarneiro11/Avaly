package com.guicarneirodev.goniometro

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.functions

class FirebaseAuthManager {
    private var firebaseAuth = FirebaseAuth.getInstance()

    fun resetPassword(email: String): String {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Endereço de e-mail inválido"
        }
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Email de redefinição de senha enviado para: $email")
                } else {
                    val exception = task.exception
                    println("Erro ao enviar email de redefinição de senha: ${exception?.message}")
                }
            }
        return "Email de redefinição de senha enviado"
    }
    fun signInEmail(email: String, password: String, callback: (Result<Unit>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                println("Login bem-sucedido: $email")
                callback(Result.success(Unit))
            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidUserException ||
                    exception is FirebaseAuthInvalidCredentialsException)
                {
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
    val firebaseAuthManager = FirebaseAuthManager()
    val context = LocalContext.current
    val sharedPreferences = LocalContext.current.getSharedPreferences("", Context.MODE_PRIVATE)

    var email by remember { mutableStateOf(sharedPreferences.getString("email", "") ?: "") }
    var lembrarEmail by remember { mutableStateOf(sharedPreferences.getBoolean("lembrarEmail", false)) }

    var password by remember { mutableStateOf(sharedPreferences.getString("senha", "") ?: "") }
    var lembrarSenha by remember { mutableStateOf(sharedPreferences.getBoolean("lembrarSenha", false)) }
    var showResetPassword by remember { mutableStateOf(false) }
    var passwordVisibility by remember { mutableStateOf(false) }

    var loginError by remember { mutableStateOf(false) }
    val errorMessage = if (loginError) {
        "Email ou senha incorretos. Por favor, tente novamente."
    } else {
        null
    }

    var securityCodeInput by remember { mutableStateOf("") }
    var invalidCode by remember { mutableStateOf("") }

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
                        if (showResetPassword) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.voltar),
                                    contentDescription = "Voltar Tela",
                                    modifier = Modifier
                                        .clickable { showResetPassword = false }
                                        .size(40.dp),
                                    tint = Color(0xFF000000)
                                )
                                Spacer(modifier = Modifier.weight(0.9f))
                                Text(
                                    text = "Redefinição de Senha",
                                    color = Color(0xFF3F48CC),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.SansSerif,
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
                            Button(
                                onClick = {
                                    val requestData = hashMapOf(
                                        "email" to email
                                    )
                                    val functions = Firebase.functions
                                    val sendEmailFunction = functions.getHttpsCallable("sendEmail")
                                    sendEmailFunction.call(requestData)
                                        .addOnSuccessListener { result ->
                                            val responseData = result.data
                                            if (responseData is Map<*, *>) {
                                                val success = responseData["success"] as? Boolean
                                                if (success == true) {
                                                    Log.d(TAG, "Email enviado com sucesso.")
                                                } else {
                                                    val error = responseData["error"] as? String ?: "Erro desconhecido"
                                                    Log.e(TAG, "Erro ao enviar email: $error")
                                                }
                                            } else {
                                                Log.e(TAG, "Resposta inesperada do servidor")
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Erro ao chamar a função do Firebase Functions: ${e.message}")
                                            if (e is FirebaseFunctionsException) {
                                                Log.e(TAG, "Código do erro: ${e.code}")
                                                Log.e(TAG, "Detalhes do erro: ${e.details}")
                                            }
                                        }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3280C4)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            ) {
                                Text(text = "Enviar Código para o Email",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.SansSerif)
                            }
                            TextField(
                                value = securityCodeInput,
                                onValueChange = { securityCodeInput = it },
                                label = { Text("Código de Segurança") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                            )
                            Button(
                                onClick = {
                                    if (securityCodeInput.isNotBlank()) {
                                        val functions = Firebase.functions
                                        val verifyCodeFunction = functions.getHttpsCallable("verifySecurityCode")
                                        val requestData = hashMapOf(
                                            "email" to email,
                                            "code" to securityCodeInput
                                        )
                                        verifyCodeFunction.call(requestData)
                                            .addOnSuccessListener { result ->
                                                val responseData = result.data
                                                if (responseData is Map<*, *>) {
                                                    val success = responseData["success"] as? Boolean
                                                    if (success == true) {
                                                        invalidCode = ""
                                                        firebaseAuthManager.resetPassword(email)
                                                        navController.popBackStack()
                                                    } else {
                                                        val error = responseData["error"] as? String ?: "Erro desconhecido"
                                                        invalidCode = "Código de segurança incorreto."
                                                        Log.e(TAG, "Erro ao verificar o código de segurança: $error")
                                                    }
                                                } else {
                                                    Log.e(TAG, "Resposta inesperada do servidor")
                                                }
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e(TAG, "Erro ao verificar o código de segurança: $e")
                                            }
                                    } else {
                                        invalidCode = "Por favor, insira um código de segurança válido."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF266399))
                            ) {
                                Text(text = "Fornecer Link para troca da senha",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.SansSerif)
                            }
                            if (invalidCode.isNotEmpty()) {
                                Text(
                                    text = invalidCode,
                                    color = Color.Red,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.voltar),
                                    contentDescription = "Voltar Tela",
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate("home")
                                        }
                                        .size(40.dp),
                                    tint = Color(0xFF000000)
                                )
                                Spacer(modifier = Modifier.weight(0.9f))
                                Spacer(modifier = Modifier.weight(1.5f))
                            }
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email",
                                    color = Color(0xFF0F0F0F),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.SansSerif) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(checked = lembrarEmail,
                                    colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF3280C4)),
                                    onCheckedChange = { lembrarEmail = it
                                        val editor = sharedPreferences.edit()
                                        editor.putBoolean("lembrarEmail", it)
                                        editor.apply()
                                    }
                                    ,modifier = Modifier.padding(start = 16.dp)
                                )
                                Text(
                                    text = "Lembrar Email",
                                    color = Color(0xFF0F0F0F),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.SansSerif,
                                    modifier = Modifier.padding(8.dp),
                                )
                            }
                            TextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Senha",
                                    color = Color(0xFF0F0F0F),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.SansSerif) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility = !passwordVisibility}) {
                                        Icon (
                                            painter  = if (passwordVisibility) painterResource(id = R.drawable.pass_off) else painterResource(id = R.drawable.pass_on),
                                            contentDescription = if (passwordVisibility) "Esconder Senha" else "Mostrar Senha",
                                            tint = Color(0xFF000000)
                                        )
                                    }
                                }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Switch(checked = lembrarSenha,
                                    colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF3280C4)),
                                    onCheckedChange = { lembrarSenha = it
                                        val editor = sharedPreferences.edit()
                                        editor.putBoolean("lembrarSenha", it)
                                        editor.apply()
                                    }
                                    ,modifier = Modifier.padding(start = 16.dp)
                                )
                                Text(
                                    text = "Lembrar Senha",
                                    color = Color(0xFF0F0F0F),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = FontFamily.SansSerif,
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
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF266399)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Entrar",
                                    color = Color(0xFFFFFFFF),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            errorMessage?.let {
                                Text(
                                    text = it,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Esqueceu sua senha?",
                                color = Color(0xFF463CDB),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.clickable { showResetPassword = true },
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Default),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}