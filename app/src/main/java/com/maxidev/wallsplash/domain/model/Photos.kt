package com.maxidev.wallsplash.domain.model

data class Photos(
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlFull: String,
    val urlRegular: String,
    val userId: String,
    val name: String,
    val profileImageLarge: String
)