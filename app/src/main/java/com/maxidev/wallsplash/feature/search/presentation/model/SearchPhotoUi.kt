package com.maxidev.wallsplash.feature.search.presentation.model

import java.util.UUID

data class SearchPhotoUi(
    val photoId: UUID,
    val id: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blurHash: String,
    val urlRaw: String,
    val urlFull: String,
    val urlRegular: String,
    val urlSmall: String,
    val urlThumb: String
)