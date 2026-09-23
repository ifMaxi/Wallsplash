package com.maxidev.wallsplash.domain.model

import java.util.UUID

data class SearchPhoto(
    val photoId: UUID = UUID.randomUUID(),
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlRegular: String
)