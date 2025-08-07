package com.champox.notes.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class AppThemeMode { SYSTEM, LIGHT, DARK }

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

class ThemePreferenceManager(private val context: Context) {

    companion object {
        private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }

    val themeModeFlow: Flow<AppThemeMode> = context.dataStore.data
        .map { preferences ->
            when (preferences[THEME_MODE_KEY]) {
                AppThemeMode.LIGHT.name -> AppThemeMode.LIGHT
                AppThemeMode.DARK.name -> AppThemeMode.DARK
                else -> AppThemeMode.SYSTEM
            }
        }

    suspend fun saveThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }
}
