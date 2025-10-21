package com.maxidev.wallsplash.feature.photos.presentation.model

data class PhotosUi(
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlFull: String,
    val urlRegular: String,
    val name: String,
    val profileImageLarge: String
)