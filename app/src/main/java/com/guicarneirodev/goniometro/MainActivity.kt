package com.guicarneirodev.goniometro

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.guicarneirodev.goniometro.domain.repository.FirebaseRepository
import com.guicarneirodev.goniometro.domain.repository.LoginRepository
import com.guicarneirodev.goniometro.presentation.ui.navigation.SetupNavGraph
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val loginRepository: LoginRepository by lazy {
        FirebaseRepository(
            FirebaseAuth.getInstance(),
            FirebaseFunctions.getInstance()
        )
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            loginRepository.getIdToken().onSuccess { token ->
                Log.d("AuthToken", "Bearer $token")
            }.onFailure { error ->
                Log.e("AuthToken", "Error getting token", error)
            }
        }

        setContent {
            val navController = rememberNavController()
            SetupNavGraph(navController = navController)
        }
    }
}