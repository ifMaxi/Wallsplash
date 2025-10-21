package com.maxidev.wallsplash.feature.photos.presentation

sealed interface PhotosUiEvents {

    // Screen events
    data object OnRefresh : PhotosUiEvents

    // Navigation events
    data object NavigateToSearch : PhotosUiEvents
    data object NavigateToFavorites : PhotosUiEvents
    data object NavigateToSettings : PhotosUiEvents
    data class NavigateToPhotoDetails(val id: String) : PhotosUiEvents
    data class NavigateToCollectionDetails(val id: String) : PhotosUiEvents
}