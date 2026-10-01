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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.maxidev.wallsplash.presentation.collections.CollectionsScreen
import com.maxidev.wallsplash.presentation.photos.PhotosScreen
import com.maxidev.wallsplash.presentation.settings.SettingsScreen
import com.maxidev.wallsplash.presentation.theme.WallsplashTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

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
    val navBackStack = rememberNavBackStack(Home)
    val currentTab = navBackStack.firstOrNull()
    val showBottomBar = navBackStack.lastOrNull() in topLevelRoutes.map { it.key }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                ShortNavigationBar {
                    topLevelRoutes.forEach { topLevelRoute ->
                        ShortNavigationBarItem(
                            selected = currentTab == topLevelRoute.key,
                            onClick = {
                                if (currentTab != topLevelRoute.key) {
                                    navBackStack.clear()

                                    if (topLevelRoute.key != Home) navBackStack.add(
                                        topLevelRoute.key
                                    )
                                    navBackStack.add(topLevelRoute.key)
                                }
                            },
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
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            backStack = navBackStack,
            onBack = { navBackStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Home> {
                    PhotosScreen(
                        navigateToDetail = { navBackStack.add(PhotoDetail(it)) },
                        navigateToUserDetail = { navBackStack.add(UserDetail(it)) }
                    )
                }
                entry<Collections> {
                    CollectionsScreen(
                        navigateToCollectionDetail = { navBackStack.add(CollectionDetail(it))},
                        navigateToUserDetail = { navBackStack.add(UserDetail(it)) }
                    )
                }
                entry<Search> {}
                entry<Favorites> {}
                entry<Settings> {
                    SettingsScreen()
                }
                entry<PhotoDetail> {}
                entry<UserDetail> {}
            }
        )
    }
}

private data class TopLevelRoute(
    val key: NavKey,
    val icon: Int,
    val label: String
)

private val topLevelRoutes = listOf(
    TopLevelRoute(key = Home, icon = R.drawable.home_app, label = "Home"),
    TopLevelRoute(key = Collections, icon = R.drawable.box, label = "Collections"),
    TopLevelRoute(key = Favorites, icon = R.drawable.favorite, label = "Favorite"),
    TopLevelRoute(key = Search, icon = R.drawable.search, label = "Search"),
    TopLevelRoute(key = Settings, icon = R.drawable.settings, label = "Setting")
)

@Serializable private data object Home : NavKey
@Serializable private data object Collections : NavKey
@Serializable private data object Favorites : NavKey
@Serializable private data object Search : NavKey
@Serializable private data object Settings : NavKey
@Serializable private data class PhotoDetail(val id: String) : NavKey
@Serializable private data class CollectionDetail(val id: String) : NavKey
@Serializable private data class UserDetail(val userId: String) : NavKey

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