package com.guicarneirodev.goniometro.domain.usecase

import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.domain.repository.LoginPreferencesRepository
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogoutUseCase(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val loginPreferencesRepository: LoginPreferencesRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val currentPreferences = userPreferencesRepository.getUserPreferences()
        val cleanPreferences = UserPreferences(
            userId = loginPreferencesRepository.getEmail(),
            language = currentPreferences.language,
            userType = UserType.STUDENT
        )
        userPreferencesRepository.saveUserPreferences(cleanPreferences)
    }
}