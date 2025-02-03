package com.guicarneirodev.goniometro.data.repository

import com.guicarneirodev.goniometro.data.local.dao.UserDao
import com.guicarneirodev.goniometro.data.local.mapper.UserMapper
import com.guicarneirodev.goniometro.domain.model.UserProfile
import com.guicarneirodev.goniometro.domain.repository.UserRepository
import javax.inject.Inject

abstract class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val mapper: UserMapper
) : UserRepository {
    override suspend fun getUserProfile(email: String): UserProfile? =
        userDao.getUserProfile(email)?.let { mapper.toDomain(it) }
}