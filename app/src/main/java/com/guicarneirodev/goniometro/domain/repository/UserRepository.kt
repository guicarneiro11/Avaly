package com.guicarneirodev.goniometro.domain.repository

import com.guicarneirodev.goniometro.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(email: String): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
}