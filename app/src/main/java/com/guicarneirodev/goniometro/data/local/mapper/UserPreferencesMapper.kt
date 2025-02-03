package com.guicarneirodev.goniometro.data.local.mapper

import com.guicarneirodev.goniometro.data.local.entity.UserPreferencesEntity
import com.guicarneirodev.goniometro.domain.model.UserPreferences

class UserPreferencesMapper {
    fun toDomain(entity: UserPreferencesEntity) = UserPreferences(
        userId = entity.userId,
        language = entity.language,
        theme = entity.theme,
        userType = entity.userType
    )

    fun toEntity(preferences: UserPreferences) = UserPreferencesEntity(preferences)
}