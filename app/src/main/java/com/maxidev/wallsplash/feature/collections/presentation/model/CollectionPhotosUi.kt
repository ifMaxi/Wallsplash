package com.maxidev.wallsplash.feature.collections.presentation.model

import java.util.UUID

data class CollectionPhotosUi(
    val photoId: UUID = UUID.randomUUID(),
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlFull: String,
    val urlRegular: String
)