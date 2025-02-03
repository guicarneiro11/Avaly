package com.guicarneirodev.goniometro.data.local.converters

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Theme
import com.guicarneirodev.goniometro.domain.model.UserType

@TypeConverters
class Converters {
    @TypeConverter
    fun fromLanguage(value: Language) = value.name
    @TypeConverter
    fun toLanguage(value: String) = enumValueOf<Language>(value)

    @TypeConverter
    fun fromTheme(value: Theme) = value.name
    @TypeConverter
    fun toTheme(value: String) = enumValueOf<Theme>(value)

    @TypeConverter
    fun fromUserType(value: UserType) = value.name
    @TypeConverter
    fun toUserType(value: String) = enumValueOf<UserType>(value)
}