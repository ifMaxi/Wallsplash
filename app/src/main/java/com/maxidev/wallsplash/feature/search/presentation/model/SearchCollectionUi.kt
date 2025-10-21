package com.maxidev.wallsplash.feature.search.presentation.model

import java.util.UUID

data class SearchCollectionUi(
    val collectionId: UUID,
    val id: String,
    val title: String,
    val totalPhotos: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val coverPhoto: String
)