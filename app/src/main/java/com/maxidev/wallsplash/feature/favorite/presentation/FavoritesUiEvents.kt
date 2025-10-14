package com.maxidev.wallsplash.feature.favorite.presentation

import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi
import java.util.UUID

sealed interface FavoritesUiEvents {
    data class DeletePhoto(val remove: FavoritesUi) : FavoritesUiEvents
    data class DeleteMorePhotos(val removeMore: List<UUID>) : FavoritesUiEvents
}