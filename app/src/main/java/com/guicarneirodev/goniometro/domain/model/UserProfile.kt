package com.guicarneirodev.goniometro.domain.model

data class UserProfile(
    val name: String,
    val email: String,
    val userType: UserType
)