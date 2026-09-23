package com.maxidev.wallsplash.domain.model

data class Collections(
    val id: String,
    val title: String,
    val totalPhotos: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlRegular: String,
    val userId: String,
    val name: String,
    val profileImageLarge: String
)