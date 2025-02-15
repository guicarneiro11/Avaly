package com.guicarneirodev.goniometro.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val email: String,
    val name: String,
    @ColumnInfo(name = "user_type")
    val userType: UserType
)