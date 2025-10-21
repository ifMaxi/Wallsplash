package com.maxidev.wallsplash.feature.photos.presentation.model

data class CollectionsUi(
    val id: String,
    val title: String,
    val totalPhotos: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlRegular: String,
    val name: String,
    val profileImageLarge: String
)