// ui/preferences/FilterPreferences.kt
package com.champox.notes.ui.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "filter_preferences")

class FilterPreferences(private val context: Context) {
    companion object {
        private val FILTER_VISIBLE_KEY = booleanPreferencesKey("filter_visible")
    }

    val filterVisibleFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[FILTER_VISIBLE_KEY] ?: true }

    suspend fun setFilterVisible(visible: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FILTER_VISIBLE_KEY] = visible
        }
    }
}