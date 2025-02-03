package com.guicarneirodev.goniometro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Theme
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserType

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val userId: String,
    val language: Language,
    val theme: Theme,
    val userType: UserType
) {
    constructor(preferences: UserPreferences) : this(
        userId = preferences.userId,
        language = preferences.language,
        theme = preferences.theme,
        userType = preferences.userType
    )
}
