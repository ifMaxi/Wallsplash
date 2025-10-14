package com.maxidev.wallsplash.feature.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable data object PhotosView : Destinations
    @Serializable data object SearchView : Destinations
    @Serializable data object FavouritesView : Destinations
    @Serializable data object PreferencesView : Destinations
    @Serializable data class CollectionsView(val id: String) : Destinations
    @Serializable data class PhotoDetailsView(val id: String) : Destinations
}