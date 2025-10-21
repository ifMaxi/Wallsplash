package com.maxidev.wallsplash.feature.search.presentation.model

import java.util.UUID

data class SearchPhotoUi(
    val photoId: UUID,
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlRegular: String
)