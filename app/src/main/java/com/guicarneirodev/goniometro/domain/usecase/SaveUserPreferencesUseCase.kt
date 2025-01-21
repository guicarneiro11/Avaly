package com.guicarneirodev.goniometro.domain.usecase

import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class SaveUserPreferencesUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    open suspend operator fun invoke(preferences: UserPreferences) = withContext(Dispatchers.IO) {
        userPreferencesRepository.saveUserPreferences(preferences)
    }
}