package com.maxidev.wallsplash.domain.model

import java.util.UUID

data class Favorites(
    val id: UUID,
    val photo: String,
    val width: Int,
    val height: Int,
    val blurHash: String
)