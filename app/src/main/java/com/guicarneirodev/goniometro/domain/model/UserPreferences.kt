package com.guicarneirodev.goniometro.domain.model

data class UserPreferences(
    val language: Language = Language.PORTUGUESE,
    val theme: Theme = Theme.SYSTEM,
    val userType: UserType = UserType.STUDENT
)

enum class Language { PORTUGUESE, ENGLISH }
enum class Theme { LIGHT, DARK, SYSTEM }
enum class UserType { STUDENT, PROFESSIONAL }