package com.maxidev.wallsplash.feature.favorite.presentation.model

import java.util.UUID

data class FavoritesUi(
    val photoId: UUID = UUID.randomUUID(),
    val photo: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val blurHash: String = ""
)