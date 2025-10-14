package com.maxidev.wallsplash.feature.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.maxidev.wallsplash.feature.collections.presentation.CollectionScreen
import com.maxidev.wallsplash.feature.detail.presentation.PhotoDetailScreen
import com.maxidev.wallsplash.feature.favorite.presentation.FavoriteScreen
import com.maxidev.wallsplash.feature.photos.presentation.PhotosScreen
import com.maxidev.wallsplash.feature.search.presentation.SearchScreen
import com.maxidev.wallsplash.feature.settings.presentation.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    starDestinations: Destinations = Destinations.PhotosView
) {
    NavHost(
        navController = navController,
        startDestination = starDestinations
    ) {
        composable<Destinations.PhotosView> {
            PhotosScreen(
                navigateToPhotoDetail = { id ->
                    navController.navigate(Destinations.PhotoDetailsView(id))
                },
                navigateToCollections = { id ->
                    navController.navigate(Destinations.CollectionsView(id))
                },
                navigateToSearch = { navController.navigate(Destinations.SearchView) },
                navigateToFavorites = { navController.navigate(Destinations.FavouritesView) },
                navigateToSettings = { navController.navigate(Destinations.PreferencesView) }
            )
        }
        composable<Destinations.CollectionsView> {
            CollectionScreen(
                popBack = { navController.popBackStack() },
                navigateToDetail = { id ->
                    navController.navigate(Destinations.PhotoDetailsView(id))
                }
            )
        }
        composable<Destinations.SearchView> {
            SearchScreen(
                navigateToDetail = { id ->
                    navController.navigate(Destinations.PhotoDetailsView(id))
                }
            )
        }
        composable<Destinations.FavouritesView> {
            FavoriteScreen()
        }
        composable<Destinations.PreferencesView> {
            SettingsScreen()
        }
        composable<Destinations.PhotoDetailsView> {
            PhotoDetailScreen()
        }
    }
}
