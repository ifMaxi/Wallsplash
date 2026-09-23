package com.maxidev.wallsplash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.maxidev.wallsplash.presentation.theme.WallsplashTheme
import com.maxidev.wallsplash.presentation.collections.CollectionsScreen
import com.maxidev.wallsplash.presentation.photos.PhotosScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            WallsplashTheme {
                RequestPermissions()

                Surface {
                    NavigationGraph()
                }
            }
        }
    }
}

@Composable
private fun NavigationGraph() {
    val topLevelBackStack = remember { TopLevelBackStack<Any>(Home) }

    Scaffold(
        bottomBar = {
            ShortNavigationBar {
                TOP_LEVEL_ROUTES.forEach { topLevelRoute ->
                    val isSelected = topLevelRoute == topLevelBackStack.topLevelKey

                    ShortNavigationBarItem(
                        selected = isSelected,
                        onClick = { topLevelBackStack.addTopLevel(topLevelRoute) },
                        icon = {
                            Icon(
                                painter = painterResource(topLevelRoute.icon),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = topLevelRoute.label) }
                    )
                }
            }
        }
    ) { _ ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Home> {
                    PhotosScreen(
                        navigateToDetail = { topLevelBackStack.add(PhotoDetail(it)) },
                        navigateToUserDetail = { topLevelBackStack.add(UserDetail(it)) }
                    )
                }
                entry<Collections> {
                    CollectionsScreen()
                }
                entry<Search> {}
                entry<Favorites> {}
                entry<Settings> {}
                entry<PhotoDetail> {}
                entry<UserDetail> {}
            }
        )
    }
}

private sealed interface TopLevelRoute {
    val icon: Int
    val label: String
}

private val TOP_LEVEL_ROUTES = listOf(Home, Collections, Favorites, Search, Settings)

private data object Home : TopLevelRoute {
    override val icon = R.drawable.home_app
    override val label = "Home"
}
private data object Collections : TopLevelRoute {
    override val icon = R.drawable.box
    override val label = "Collections"
}
private data object Favorites : TopLevelRoute {
    override val icon = R.drawable.favorite
    override val label = "Favorite"
}
private data object Search : TopLevelRoute {
    override val icon = R.drawable.search
    override val label = "Search"
}
private data object Settings : TopLevelRoute {
    override val icon = R.drawable.settings
    override val label = "Setting"
}
private data class PhotoDetail(val id: String)
private data class UserDetail(val userId: String)

private class TopLevelBackStack<T: Any>(startKey: T) {

    private var topLevelStacks : LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    var topLevelKey by mutableStateOf(startKey)
        private set

    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    fun addTopLevel(key: T){
        if (topLevelStacks[key] == null){
            topLevelStacks.put(key, mutableStateListOf(key))
        } else {
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: T){
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast(){
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }
}

/**
 * Request app permissions if they weren't granted the first time.
 * Currently, it only requires notification permissions.
 */
@Composable
private fun RequestPermissions() {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                }
            }
        )

        LaunchedEffect(Unit) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {}

                else -> {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}