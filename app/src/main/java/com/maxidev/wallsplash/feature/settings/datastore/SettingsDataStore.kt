package com.maxidev.wallsplash.feature.settings.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

private const val SETTINGS_DATASTORE = "settings"

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS_DATASTORE)

enum class SettingsType {
    SYSTEM,
    DARK,
    LIGHT
}

class SettingsDataStore(context: Context) {

    private val dataStore = context.settingsDataStore

    private object PreferencesKey {
        val DYNAMIC_THEME = booleanPreferencesKey("dynamic_theme")
        val THEME = intPreferencesKey("theme")
    }

    val isDynamicThemeFlow: Flow<Boolean> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[PreferencesKey.DYNAMIC_THEME] == true
            }

    val themeTypeFlow: Flow<SettingsType> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                SettingsType.entries[preferences[PreferencesKey.THEME] ?: 0]
            }

    fun setDynamicTheme(value: Boolean, scope: CoroutineScope) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKey.DYNAMIC_THEME] = value
            }
        }
    }

    fun setThemeType(value: SettingsType, scope: CoroutineScope) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKey.THEME] = value.ordinal
            }
        }
    }
}