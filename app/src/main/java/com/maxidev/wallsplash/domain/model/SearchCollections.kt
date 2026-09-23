package com.maxidev.wallsplash.domain.model

import java.util.UUID

data class SearchCollections(
    val collectionId: UUID = UUID.randomUUID(),
    val id: String,
    val title: String,
    val totalPhotos: Int,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val coverPhoto: String
)