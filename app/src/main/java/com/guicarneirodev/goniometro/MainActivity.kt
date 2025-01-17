package com.guicarneirodev.goniometro

import android.content.Context
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
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.repository.FirebaseRepository
import com.guicarneirodev.goniometro.domain.repository.LoginRepository
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import com.guicarneirodev.goniometro.presentation.ui.navigation.SetupNavGraph
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val loginRepository: LoginRepository by lazy {
        FirebaseRepository(
            FirebaseAuth.getInstance(), FirebaseFunctions.getInstance()
        )
    }

    private val userPreferencesRepository: UserPreferencesRepository by inject()

    override fun attachBaseContext(newBase: Context) {
        val preferences = runBlocking {
            userPreferencesRepository.getUserPreferences()
        }

        val locale = when (preferences.language) {
            Language.ENGLISH -> Locale("en")
            Language.PORTUGUESE -> Locale("pt")
        }

        val config = newBase.resources.configuration
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLanguageChanges()

        setContent {
            val navController = rememberNavController()
            SetupNavGraph(navController = navController)
        }
    }

    private fun observeLanguageChanges() {
        lifecycleScope.launch {
            userPreferencesRepository.getUserPreferencesFlow().collect { preferences ->
                if (preferences.language != currentLocale()) {
                    recreate()
                }
            }
        }
    }

    private fun currentLocale(): Language {
        return if (resources.configuration.locales[0].language == "en") {
            Language.ENGLISH
        } else {
            Language.PORTUGUESE
        }
    }
}