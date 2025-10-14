package com.maxidev.wallsplash.feature.photos.domain.model

data class Collections(
    val id: String,
    val title: String,
    val totalPhotos: Int,
    val width: Int,
    val height: Int,
    val color: String,
    val blurHash: String,
    val urlRaw: String,
    val urlFull: String,
    val urlRegular: String,
    val urlSmall: String,
    val urlThumb: String,
    val userId: String,
    val name: String,
    val profileImageLarge: String
)