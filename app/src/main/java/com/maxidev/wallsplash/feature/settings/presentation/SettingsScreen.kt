package com.maxidev.wallsplash.feature.settings.presentation

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.wallsplash.feature.settings.datastore.SettingsType

// TODO: Replace github with about.

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDialogVisible by viewModel.dialogVisible.collectAsStateWithLifecycle()
    val isDynamicTheme by viewModel.isDynamicTheme.collectAsStateWithLifecycle()
    val isTheme by viewModel.isTheme.collectAsStateWithLifecycle()

    ScreenContent(
        isDynamic = isDynamicTheme,
        onVisibility = { viewModel.setDialogVisible(true) },
        updateDynamicTheme = { viewModel.updateDynamicTheme() }
    )

    if (isDialogVisible) {
        ThemesDialog(
            themeState = isTheme,
            onVisibility = { viewModel.setDialogVisible(false) },
            updateThemeType = { viewModel.updateTheme(it) }
        )
    }
}


@Composable
private fun ScreenContent(
    isDynamic: Boolean,
    onVisibility: (Boolean) -> Unit,
    updateDynamicTheme: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val browserIntent = Intent(Intent.ACTION_VIEW, "https://github.com/ifMaxi".toUri())

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderTitleItem(title = "Settings")

            OutlinedCard(
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.outlinedCardElevation(8.dp),
                shape = RoundedCornerShape(5)
            ) {
                ListItem(
                    headlineContent = { Text(text = "Theme") },
                    trailingContent = {
                        IconButton(onClick = { onVisibility(true) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Change app theme."
                            )
                        }
                    }
                )
                HorizontalDivider()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ListItem(
                        headlineContent = { Text(text = "Dynamic color") },
                        trailingContent = {
                            Switch(
                                checked = (isDynamic),
                                onCheckedChange = updateDynamicTheme
                            )
                        },
                        modifier = Modifier.clickable { updateDynamicTheme(!isDynamic) }
                    )
                }
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(text = "GitHub") },
                    trailingContent = {
                        IconButton(onClick = { context.startActivity(browserIntent) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Open GitHub."
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemesDialog(
    themeState: SettingsUiState,
    onVisibility: (Boolean) -> Unit,
    updateThemeType: (SettingsType) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onVisibility(false) },
        title = {
            Text(
                text = "Choose theme"
            )
        },
        text = {
            RadioButtons(
                state = themeState,
                updateThemeType = updateThemeType
            )
        },
        confirmButton = {
            TextButton(onClick = { onVisibility(false) }) {
                Text(
                    text = "Confirm"
                )
            }
        }
    )
}

@Composable
private fun RadioButtons(
    state: SettingsUiState,
    updateThemeType: (SettingsType) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        state.radioItems.forEach {
            Row(
                modifier = Modifier.selectable(
                    selected = (it.value == state.selectedRadio),
                    onClick = { updateThemeType(it.value) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (it.value == state.selectedRadio),
                    onClick = { updateThemeType(it.value) }
                )
                Text(text = it.title)
            }
        }
    }
}

@Composable
fun HeaderTitleItem(title: String) {
    Box(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}