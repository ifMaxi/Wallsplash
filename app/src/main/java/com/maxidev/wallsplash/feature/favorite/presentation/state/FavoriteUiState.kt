package com.maxidev.wallsplash.feature.favorite.presentation.state

import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi

data class FavoriteUiState(
    val favorites: List<FavoritesUi> = emptyList()
)