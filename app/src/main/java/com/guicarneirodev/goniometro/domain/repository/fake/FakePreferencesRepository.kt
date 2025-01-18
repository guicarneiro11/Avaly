package com.guicarneirodev.goniometro.domain.repository.fake

import com.guicarneirodev.goniometro.domain.repository.LoginPreferencesRepository

class FakePreferencesRepository : LoginPreferencesRepository {
    private var email = ""
    private var password = ""
    private var rememberEmail = false
    private var rememberPassword = false

    override fun getEmail(): String = email
    override fun setEmail(email: String) { this.email = email }
    override fun getPassword(): String = password
    override fun setPassword(password: String) { this.password = password }
    override fun getRememberEmail(): Boolean = rememberEmail
    override fun setRememberEmail(remember: Boolean) { this.rememberEmail = remember }
    override fun getRememberPassword(): Boolean = rememberPassword
    override fun setRememberPassword(remember: Boolean) { this.rememberPassword = remember }
    override fun clearCredentials() {
        email = ""
        password = ""
    }
}