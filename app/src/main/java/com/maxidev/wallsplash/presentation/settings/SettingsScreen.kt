package com.maxidev.wallsplash.presentation.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.data.datastore.SettingsType
import com.maxidev.wallsplash.utils.Constants.GIT_HUB_PAGE
import com.maxidev.wallsplash.utils.Constants.UNSPLASH_API_PAGE
import com.maxidev.wallsplash.utils.Constants.UNSPLASH_OFFICIAL_PAGE

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val isDialogVisible by viewModel.dialogVisible.collectAsStateWithLifecycle()
    val isProjectDialogVisible by viewModel.projectDialogVisible.collectAsStateWithLifecycle()
    val isDynamicTheme by viewModel.isDynamicTheme.collectAsStateWithLifecycle()
    val isTheme by viewModel.isTheme.collectAsStateWithLifecycle()

    ScreenContent(
        isDynamic = isDynamicTheme,
        onVisibility = { viewModel.setDialogVisible(true) },
        onProjectVisibility = { viewModel.setProjectDialogVisible(true) },
        updateDynamicTheme = { viewModel.updateDynamicTheme() }
    )

    if (isDialogVisible) {
        ThemesDialog(
            themeState = isTheme,
            onVisibility = { viewModel.setDialogVisible(false) },
            updateThemeType = { viewModel.updateTheme(it) }
        )
    }

    if (isProjectDialogVisible) {
        AboutProjectDialog(onProjectVisibility = { viewModel.setProjectDialogVisible(false) })
    }
}

@Composable
private fun ScreenContent(
    isDynamic: Boolean,
    onVisibility: (Boolean) -> Unit,
    onProjectVisibility: (Boolean) -> Unit,
    updateDynamicTheme: (Boolean) -> Unit
) {
    val lazyState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollState = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollState.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Settings") },
                scrollBehavior = scrollState
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyState,
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PreferencesListItem(
                    isDynamic = isDynamic,
                    onVisibility = onVisibility,
                    updateDynamicTheme = updateDynamicTheme
                )
            }
            item {
                PermissionListItem()
            }
            item {
                AboutListItem(onProjectVisibility = onProjectVisibility)
            }
            item {
                MessageBoxItem()
            }
        }
    }
}

@Composable
private fun PreferencesListItem(
    modifier: Modifier = Modifier,
    isDynamic: Boolean,
    onVisibility: (Boolean) -> Unit,
    updateDynamicTheme: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isSwitchPressed by interactionSource.collectIsPressedAsState()
    val itemList = listOf(
        PreferencesList(
            content = "Theme",
            trailingContent = {
                IconButton(onClick = { onVisibility(true) }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_circle_right),
                        contentDescription = "Select theme."
                    )
                }
            },
            leadingContent = painterResource(R.drawable.palette),
            supportingContent = "Change system theme.",
            buttonContentDescription = "Open the dialog box with the themes to select."
        ),
        PreferencesList(
            content = "Dynamic color",
            trailingContent = {
                Switch(
                    checked = (isDynamic),
                    onCheckedChange = updateDynamicTheme,
                    modifier = Modifier
                        .selectable(
                            selected = isSwitchPressed,
                            interactionSource = interactionSource,
                            onClick = { updateDynamicTheme(!isDynamic) }
                        )
                )
            },
            leadingContent = painterResource(R.drawable.colors),
            supportingContent = "On/Off dynamic color.",
            buttonContentDescription = "Switch between dynamic or normal color."
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderTitleItem(title = "Preferences")

        itemList.forEach { item ->
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = ListItemDefaults.elevation(8.dp),
                shapes = ListItemDefaults.shapes(RoundedCornerShape(20.dp)),
                leadingContent = {
                    Icon(
                        painter = item.leadingContent,
                        contentDescription = null
                    )
                },
                trailingContent = item.trailingContent,
                supportingContent = { Text(text = item.supportingContent) },
                content = { Text(text = item.content) }
            )
        }
    }
}

@Stable
private data class PreferencesList(
    val content: String,
    val trailingContent: @Composable (() -> Unit)? = null,
    val leadingContent: Painter,
    val supportingContent: String,
    val buttonContentDescription: String
)

@Composable
private fun PermissionListItem(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderTitleItem(title = "Permissions")

        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = ListItemDefaults.elevation(8.dp),
            shapes = ListItemDefaults.shapes(RoundedCornerShape(20.dp)),
            content = { Text(text = "Notifications") },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.notifications),
                    contentDescription = "Notifications."
                )
            },
            supportingContent = { Text(text = "Turn on/off notifications.") },
            trailingContent = {
                IconButton(onClick = { context.startActivity(intent) }) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_circle_right),
                        contentDescription = "Go to settings."
                    )
                }
            }
        )
    }
}

@Composable
private fun AboutListItem(
    modifier: Modifier = Modifier,
    onProjectVisibility: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val actionViewIntent = Intent.ACTION_VIEW
    val gitHubIntent = Intent(actionViewIntent, GIT_HUB_PAGE.toUri())
    val unsplashApiIntent = Intent(actionViewIntent, UNSPLASH_API_PAGE.toUri())
    val unsplashPageIntent = Intent(actionViewIntent, UNSPLASH_OFFICIAL_PAGE.toUri())
    val itemList = listOf(
        AboutList(
            content = "Project",
            supportingText = "About the project.",
            leadingContent = painterResource(R.drawable.help),
            trailingContent = {
                IconButton(onClick = { onProjectVisibility(true) }) {
                    Icon(
                        painter = painterResource(R.drawable.live_help),
                        contentDescription = "About the project."
                    )
                }
            }
        ),
        AboutList(
            content = "GitHub",
            supportingText = "Open my GitHub profile.",
            leadingContent = painterResource(R.drawable.commit),
            trailingContent = {
                IconButton(onClick = { context.startActivity(gitHubIntent) }) {
                    Icon(
                        painter = painterResource(R.drawable.open_in_new),
                        contentDescription = "Open GitHub."
                    )
                }
            }
        ),
        AboutList(
            content = "Powered by Unsplash",
            supportingText = "Unsplash API.",
            leadingContent = painterResource(R.drawable.api),
            trailingContent = {
                IconButton(onClick = { context.startActivity(unsplashApiIntent) }) {
                    Icon(
                        painter = painterResource(R.drawable.open_in_new),
                        contentDescription = "Go to Unsplash api page."
                    )
                }
            }
        ),
        AboutList(
            content = "Unsplash",
            supportingText = "Official Unsplash page.",
            leadingContent = painterResource(R.drawable.web),
            trailingContent = {
                IconButton(onClick = { context.startActivity(unsplashPageIntent) }) {
                    Icon(
                        painter = painterResource(R.drawable.open_in_new),
                        contentDescription = "Go to unsplash official page."
                    )
                }
            }
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderTitleItem(title = "About")

        itemList.forEach { item ->
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = ListItemDefaults.elevation(8.dp),
                shapes = ListItemDefaults.shapes(RoundedCornerShape(20.dp)),
                content = { Text(text = item.content) },
                supportingContent = { Text(text = item.supportingText) },
                leadingContent = {
                    Icon(
                        painter = item.leadingContent,
                        contentDescription = null
                    )
                },
                trailingContent = item.trailingContent
            )
        }
    }
}

@Stable
private data class AboutList(
    val content: String,
    val leadingContent: Painter,
    val trailingContent: @Composable (() -> Unit)? = null,
    val supportingText: String
)

@Composable
private fun MessageBoxItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Made with ♥️ by Maximiliano.",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
private fun AboutProjectDialog(onProjectVisibility: (Boolean) -> Unit) {
    val projectDescription = R.string.project_description

    AlertDialog(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = { onProjectVisibility(false) },
        title = { Text(text = "About the project") },
        text = {
            Text(
                text = stringResource(projectDescription),
                textAlign = TextAlign.Start
            )
        },
        confirmButton = {
            TextButton(onClick = { onProjectVisibility(false) }) {
                Text(text = "Dismiss")
            }
        }
    )
}

@Composable
private fun ThemesDialog(
    themeState: SettingsUiState,
    onVisibility: (Boolean) -> Unit,
    updateThemeType: (SettingsType) -> Unit
) {
    AlertDialog(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        onDismissRequest = { onVisibility(false) },
        title = { Text(text = "Select system theme") },
        text = {
            ChoseThemeRadioButtons(
                state = themeState,
                updateThemeType = updateThemeType
            )
        },
        confirmButton = {
            TextButton(onClick = { onVisibility(false) }) {
                Text(text = "Confirm")
            }
        }
    )
}

@Composable
private fun ChoseThemeRadioButtons(
    state: SettingsUiState,
    updateThemeType: (SettingsType) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        state.radioItems.forEach {
            ListItem(
                modifier = Modifier
                    .selectable(
                        selected = it.value == state.selectedRadio,
                        onClick = { updateThemeType(it.value) }
                    ),
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                leadingContent = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = it.title
                    )
                },
                trailingContent = {
                    RadioButton(
                        selected = (it.value == state.selectedRadio),
                        onClick = { updateThemeType(it.value) }
                    )
                },
                content = { Text(text = it.title) }
            )
        }
    }
}

@Composable
fun HeaderTitleItem(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

/* Preview composables. */

@Preview(showBackground = true)
@Composable
private fun HeaderTitlePreview() {
    HeaderTitleItem(title = "Lorem impsum")
}

@Preview(showBackground = true)
@Composable
private fun PreferencesItemPreview() {
    PreferencesListItem(
        isDynamic = false,
        onVisibility = {},
        updateDynamicTheme = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionItemPreview() {
    PermissionListItem()
}

@Preview(showBackground = true)
@Composable
private fun AboutItemPreview() {
    AboutListItem(
        onProjectVisibility = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MessageBoxPreview() {
    MessageBoxItem()
}

@Preview
@Composable
private fun AboutProjectPreview() {
    val openDialog by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        if (openDialog) {
            AboutProjectDialog(onProjectVisibility = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenContentPreview() {
    ScreenContent(
        isDynamic = false,
        onVisibility = {},
        updateDynamicTheme = {},
        onProjectVisibility = {}
    )
}