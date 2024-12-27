package com.guicarneirodev.goniometro.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.guicarneirodev.goniometro.domain.model.Language
import com.guicarneirodev.goniometro.domain.model.Theme
import com.guicarneirodev.goniometro.domain.model.UserPreferences
import com.guicarneirodev.goniometro.domain.model.UserType
import com.guicarneirodev.goniometro.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    context: Context
) : UserPreferencesRepository {
    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("user_preferences") }
    )

    companion object {
        private val LANGUAGE = stringPreferencesKey("language")
        private val USER_TYPE = stringPreferencesKey("user_type")
        private val THEME = stringPreferencesKey("theme")
    }

    override suspend fun getUserPreferences(): UserPreferences {
        return dataStore.data.map { preferences ->
            UserPreferences(
                language = try {
                    Language.valueOf(preferences[LANGUAGE] ?: Language.PORTUGUESE.name)
                } catch (e: Exception) {
                    Language.PORTUGUESE
                },
                userType = try {
                    UserType.valueOf(preferences[USER_TYPE] ?: UserType.STUDENT.name)
                } catch (e: Exception) {
                    UserType.STUDENT
                },
                theme = try {
                    Theme.valueOf(preferences[THEME] ?: Theme.SYSTEM.name)
                } catch (e: Exception) {
                    Theme.SYSTEM
                }
            )
        }.first()
    }

    override suspend fun saveUserPreferences(preferences: UserPreferences) {
        dataStore.edit { prefs ->
            prefs[LANGUAGE] = preferences.language.name
            prefs[USER_TYPE] = preferences.userType.name
            prefs[THEME] = preferences.theme.name
        }
    }

    override suspend fun updateLanguage(language: Language) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = language.name
        }
    }

    override suspend fun updateUserType(userType: UserType) {
        dataStore.edit { preferences ->
            preferences[USER_TYPE] = userType.name
        }
    }

    override fun getUserPreferencesFlow(): Flow<UserPreferences> {
        return dataStore.data.map { preferences ->
            UserPreferences(
                language = try {
                    Language.valueOf(preferences[LANGUAGE] ?: Language.PORTUGUESE.name)
                } catch (e: Exception) {
                    Language.PORTUGUESE
                },
                userType = try {
                    UserType.valueOf(preferences[USER_TYPE] ?: UserType.STUDENT.name)
                } catch (e: Exception) {
                    UserType.STUDENT
                },
                theme = try {
                    Theme.valueOf(preferences[THEME] ?: Theme.SYSTEM.name)
                } catch (e: Exception) {
                    Theme.SYSTEM
                }
            )
        }
    }
}