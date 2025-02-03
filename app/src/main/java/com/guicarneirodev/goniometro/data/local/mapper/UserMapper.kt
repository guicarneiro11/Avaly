package com.guicarneirodev.goniometro.data.local.mapper

import com.guicarneirodev.goniometro.data.local.entity.UserProfileEntity
import com.guicarneirodev.goniometro.domain.model.UserProfile

class UserMapper {
    fun toDomain(entity: UserProfileEntity) = UserProfile(
        email = entity.email,
        name = entity.name,
        userType = entity.userType
    )

    fun toEntity(domain: UserProfile) = UserProfileEntity(
        email = domain.email,
        name = domain.name,
        userType = domain.userType
    )
}