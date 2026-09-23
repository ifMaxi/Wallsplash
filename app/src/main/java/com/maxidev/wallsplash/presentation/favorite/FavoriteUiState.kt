package com.maxidev.wallsplash.presentation.favorite

import com.maxidev.wallsplash.domain.model.Favorites

data class FavoriteUiState(
    val favorites: List<Favorites> = emptyList()
)