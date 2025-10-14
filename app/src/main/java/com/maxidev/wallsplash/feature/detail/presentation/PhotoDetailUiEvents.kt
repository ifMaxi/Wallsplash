package com.maxidev.wallsplash.feature.detail.presentation

import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi

interface PhotoDetailUiEvents {
    data object OnShare : PhotoDetailUiEvents
    data object OnDownload : PhotoDetailUiEvents
    data class OnSave(val save: FavoritesUi) : PhotoDetailUiEvents
}