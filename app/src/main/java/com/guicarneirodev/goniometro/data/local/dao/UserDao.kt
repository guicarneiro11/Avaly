package com.guicarneirodev.goniometro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.guicarneirodev.goniometro.data.local.entity.UserProfileEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profiles WHERE email = :email")
    suspend fun getUserProfile(email: String): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfileEntity)
}