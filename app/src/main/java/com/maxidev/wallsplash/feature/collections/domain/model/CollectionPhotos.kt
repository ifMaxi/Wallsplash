package com.maxidev.wallsplash.feature.collections.domain.model

import java.util.UUID

data class CollectionPhotos(
    val photoId: UUID = UUID.randomUUID(),
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlFull: String,
    val urlRegular: String
)