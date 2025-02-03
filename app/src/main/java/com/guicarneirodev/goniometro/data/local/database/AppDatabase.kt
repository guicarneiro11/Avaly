package com.guicarneirodev.goniometro.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.guicarneirodev.goniometro.data.local.converters.Converters
import com.guicarneirodev.goniometro.data.local.dao.UserDao
import com.guicarneirodev.goniometro.data.local.dao.UserPreferencesDao
import com.guicarneirodev.goniometro.data.local.entity.UserPreferencesEntity
import com.guicarneirodev.goniometro.data.local.entity.UserProfileEntity

@Database(
    entities = [UserProfileEntity::class, UserPreferencesEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun preferencesDao(): UserPreferencesDao
}