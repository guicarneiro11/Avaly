package com.guicarneirodev.goniometro.presentation.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.guicarneirodev.goniometro.domain.repository.FirebaseRepository
import com.guicarneirodev.goniometro.domain.repository.SharedPreferencesRepository
import com.guicarneirodev.goniometro.presentation.viewmodel.LoginScreenViewModel

class LoginViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            val authRepository =
                FirebaseRepository(FirebaseAuth.getInstance(), FirebaseFunctions.getInstance())
            val preferencesRepository = SharedPreferencesRepository(
                context.getSharedPreferences(
                    "login_prefs",
                    Context.MODE_PRIVATE
                )
            )
            @Suppress("UNCHECKED_CAST")
            return LoginScreenViewModel(authRepository, preferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}