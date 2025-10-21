package com.maxidev.wallsplash.feature.photos.domain.model

data class Collections(
    val id: String,
    val title: String,
    val totalPhotos: Int,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlRegular: String,
    val name: String,
    val profileImageLarge: String
)