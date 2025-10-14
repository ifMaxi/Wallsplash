package com.maxidev.wallsplash.feature.settings.presentation

import com.maxidev.wallsplash.feature.settings.datastore.SettingsType

data class SettingsUiState(
    val selectedRadio: SettingsType,
    val radioItems: List<RadioItem> = listOf(
        RadioItem(value = SettingsType.SYSTEM, title = "System"),
        RadioItem(value = SettingsType.DARK, title = "Dark"),
        RadioItem(value = SettingsType.LIGHT, title = "Light")
    )
)

data class RadioItem(val value:SettingsType, val title: String)