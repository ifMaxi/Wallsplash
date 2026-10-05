package com.maxidev.wallsplash.presentation.settings

import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.data.datastore.SettingsType

data class SettingsUiState(
    val selectedRadio: SettingsType,
    val radioItems: List<RadioItem> = listOf(
        RadioItem(value = SettingsType.SYSTEM, title = "System", icon = R.drawable.routine_day_night),
        RadioItem(value = SettingsType.DARK, title = "Dark", icon = R.drawable.dark_mode),
        RadioItem(value = SettingsType.LIGHT, title = "Light", icon = R.drawable.light_mode)
    )
)

data class RadioItem(val value:SettingsType, val title: String, val icon: Int)