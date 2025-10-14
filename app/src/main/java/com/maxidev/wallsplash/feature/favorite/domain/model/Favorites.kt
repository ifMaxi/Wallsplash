package com.maxidev.wallsplash.feature.favorite.domain.model

import java.util.UUID

data class Favorites(
    val id: UUID,
    val photo: String,
    val width: Int,
    val height: Int
)