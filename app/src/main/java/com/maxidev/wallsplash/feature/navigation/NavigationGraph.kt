package com.maxidev.wallsplash.feature.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.maxidev.wallsplash.feature.collections.presentation.collectionsDestination
import com.maxidev.wallsplash.feature.detail.presentation.photoDetailDestination
import com.maxidev.wallsplash.feature.favorite.presentation.favoriteDestination
import com.maxidev.wallsplash.feature.photos.presentation.photosDestination
import com.maxidev.wallsplash.feature.search.presentation.searchDestination
import com.maxidev.wallsplash.feature.settings.presentation.settingsDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
    starDestinations: Destinations = Destinations.PhotosView
) {
    val animatedContentScope = AnimatedContentTransitionScope.SlideDirection

    NavHost(
        navController = navController,
        startDestination = starDestinations,
        enterTransition = {
            slideIntoContainer(animatedContentScope.Start, tween(700))
        },
        exitTransition = {
            slideOutOfContainer(animatedContentScope.Start, tween(700))
        },
        popEnterTransition = {
            slideIntoContainer(animatedContentScope.End, tween(700))
        },
        popExitTransition = {
            slideOutOfContainer(animatedContentScope.End, tween(700))
        }
    ) {
        photosDestination(
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

        collectionsDestination(
            popBack = { navController.popBackStack() },
            navigateToDetail = { id ->
                navController.navigate(Destinations.PhotoDetailsView(id))
            }
        )

        searchDestination(
            navigateToDetail = { id ->
                navController.navigate(Destinations.PhotoDetailsView(id))
            },
            navigateToCollections = { id ->
                navController.navigate(Destinations.CollectionsView(id))
            }
        )

        favoriteDestination()

        settingsDestination()

        photoDetailDestination()
    }
}
