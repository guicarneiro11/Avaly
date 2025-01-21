package com.guicarneirodev.goniometro.domain.usecase

import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class GetUserPreferencesUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    open suspend operator fun invoke(): UserPreferences = withContext(Dispatchers.IO) {
        userPreferencesRepository.getUserPreferences()
    }
}