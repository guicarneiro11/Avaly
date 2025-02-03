package com.guicarneirodev.goniometro.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.guicarneirodev.goniometro.domain.model.UserType

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val email: String,
    val name: String,
    @ColumnInfo(name = "user_type")
    val userType: UserType
)
