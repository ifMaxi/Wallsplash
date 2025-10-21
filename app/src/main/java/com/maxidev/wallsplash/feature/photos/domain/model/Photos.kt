package com.maxidev.wallsplash.feature.photos.domain.model

data class Photos(
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlFull: String,
    val urlRegular: String,
    val name: String,
    val profileImageLarge: String
)