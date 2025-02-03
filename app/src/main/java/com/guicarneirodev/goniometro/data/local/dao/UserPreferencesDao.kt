package com.guicarneirodev.goniometro.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.guicarneirodev.goniometro.data.local.entity.UserPreferencesEntity

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    suspend fun getUserPreferences(userId: String): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preferences: UserPreferencesEntity)

    @Update
    suspend fun update(preferences: UserPreferencesEntity)

    @Delete
    suspend fun delete(preferences: UserPreferencesEntity)
}