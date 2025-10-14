package com.maxidev.wallsplash.feature.photos.presentation.model

data class PhotosUi(
    val id: String,
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