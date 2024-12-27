package com.guicarneirodev.goniometro.utils

import android.app.Activity
import android.content.Context
import com.guicarneirodev.goniometro.domain.model.Language
import java.util.Locale

class LocaleHelper(private val context: Context) {
    fun updateLocale(language: Language): Boolean {
        val locale = when (language) {
            Language.ENGLISH -> Locale("en")
            Language.PORTUGUESE -> Locale("pt")
        }

        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)

        if (context is Activity) {
            context.recreate()
        }

        return true
    }
}