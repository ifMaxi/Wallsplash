package com.maxidev.wallsplash.feature.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.feature.settings.datastore.SettingsDataStore
import com.maxidev.wallsplash.feature.settings.datastore.SettingsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(dataStore: SettingsDataStore) : ViewModel() {

    val isDynamicTheme: StateFlow<Boolean> =
        dataStore.isDynamicThemeFlow.map { it }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                true
            )

    val isDarkTheme: StateFlow<SettingsType> =
        dataStore.themeTypeFlow.map { it }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                SettingsType.SYSTEM
            )
}