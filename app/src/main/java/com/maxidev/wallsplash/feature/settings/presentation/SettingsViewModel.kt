package com.maxidev.wallsplash.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.feature.settings.datastore.SettingsDataStore
import com.maxidev.wallsplash.feature.settings.datastore.SettingsType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    val dialogVisible = MutableStateFlow(false)

    val isDynamicTheme: StateFlow<Boolean> =
        dataStore.isDynamicThemeFlow.map { it }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                true
            )

    val isTheme: StateFlow<SettingsUiState> =
        dataStore.themeTypeFlow.map { SettingsUiState(it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                SettingsUiState(SettingsType.SYSTEM)
            )

    fun setDialogVisible(value: Boolean) {
        dialogVisible.value = value
    }

    fun updateDynamicTheme() {
        dataStore.setDynamicTheme(!isDynamicTheme.value, viewModelScope)
    }

    fun updateTheme(value: SettingsType) {
        dataStore.setThemeType(value, viewModelScope)
    }
}