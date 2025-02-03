package com.guicarneirodev.goniometro.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.guicarneirodev.goniometro.data.local.dao.UserPreferencesDao
import com.guicarneirodev.goniometro.data.local.mapper.UserPreferencesMapper
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Theme
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.domain.repository.LoginPreferencesRepository
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    private val preferencesDao: UserPreferencesDao,
    private val dataStore: DataStore<Preferences>,
    private val loginPreferences: LoginPreferencesRepository,
    private val mapper: UserPreferencesMapper
) : UserPreferencesRepository {
    private fun getCurrentUserId(): String = loginPreferences.getEmail()

    companion object {
        private val LANGUAGE = stringPreferencesKey("language")
        private val USER_TYPE = stringPreferencesKey("user_type")
        private val THEME = stringPreferencesKey("theme")
    }

    override suspend fun getUserPreferences(): UserPreferences {
        val preferences = dataStore.data.first()
        return UserPreferences(
            userId = getCurrentUserId(),
            language = Language.valueOf(preferences[LANGUAGE] ?: Language.PORTUGUESE.name),
            userType = UserType.valueOf(preferences[USER_TYPE] ?: UserType.STUDENT.name),
            theme = Theme.valueOf(preferences[THEME] ?: Theme.SYSTEM.name)
        )
    }

    override suspend fun saveUserPreferences(preferences: UserPreferences) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE] = preferences.language.name
            prefs[USER_TYPE] = preferences.userType.name
            prefs[THEME] = preferences.theme.name
        }
        preferencesDao.insert(mapper.toEntity(preferences))
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language.name
        }
        val currentPrefs = getUserPreferences()
        saveUserPreferences(currentPrefs.copy(language = language))
    }

    override suspend fun updateUserType(userType: UserType) {
        dataStore.edit { preferences ->
            preferences[USER_TYPE] = userType.name
        }
        val currentPrefs = getUserPreferences()
        saveUserPreferences(currentPrefs.copy(userType = userType))
    }

    override fun getUserPreferencesFlow(): Flow<UserPreferences> =
        dataStore.data.map { preferences ->
            UserPreferences(
                userId = getCurrentUserId(),
                language = Language.valueOf(preferences[LANGUAGE] ?: Language.PORTUGUESE.name),
                userType = UserType.valueOf(preferences[USER_TYPE] ?: UserType.STUDENT.name),
                theme = Theme.valueOf(preferences[THEME] ?: Theme.SYSTEM.name)
            )
        }
}