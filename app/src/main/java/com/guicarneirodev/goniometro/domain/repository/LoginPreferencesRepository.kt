package com.guicarneirodev.goniometro.domain.repository

import android.content.SharedPreferences

interface LoginPreferencesRepository {
    fun getEmail(): String
    fun setEmail(email: String)
    fun getPassword(): String
    fun setPassword(password: String)
    fun getRememberEmail(): Boolean
    fun setRememberEmail(remember: Boolean)
    fun getRememberPassword(): Boolean
    fun setRememberPassword(remember: Boolean)
    fun clearCredentials()
}

class SharedPreferencesRepository(private val sharedPreferences: SharedPreferences) :
    LoginPreferencesRepository {
    override fun getEmail(): String = sharedPreferences.getString("email", "") ?: ""
    override fun setEmail(email: String) =
        sharedPreferences.edit().putString("email", email).apply()

    override fun getPassword(): String = sharedPreferences.getString("senha", "") ?: ""
    override fun setPassword(password: String) =
        sharedPreferences.edit().putString("senha", password).apply()

    override fun getRememberEmail(): Boolean = sharedPreferences.getBoolean("lembrarEmail", false)
    override fun setRememberEmail(remember: Boolean) =
        sharedPreferences.edit().putBoolean("lembrarEmail", remember).apply()

    override fun getRememberPassword(): Boolean =
        sharedPreferences.getBoolean("lembrarSenha", false)

    override fun setRememberPassword(remember: Boolean) =
        sharedPreferences.edit().putBoolean("lembrarSenha", remember).apply()

    override fun clearCredentials() =
        sharedPreferences.edit().remove("email").remove("senha").apply()
}