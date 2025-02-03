package com.guicarneirodev.goniometro.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey val userId: String,
    @ColumnInfo(name = "language")
    val language: Language = Language.PORTUGUESE,
    @ColumnInfo(name = "theme")
    val theme: Theme = Theme.SYSTEM,
    @ColumnInfo(name = "user_type")
    val userType: UserType = UserType.STUDENT
)

enum class Language { PORTUGUESE, ENGLISH }
enum class Theme { LIGHT, DARK, SYSTEM }
enum class UserType { STUDENT, PROFESSIONAL }