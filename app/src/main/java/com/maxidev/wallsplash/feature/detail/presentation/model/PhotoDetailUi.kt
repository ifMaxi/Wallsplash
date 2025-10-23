package com.maxidev.wallsplash.feature.detail.presentation.model

data class PhotoDetailUi(
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val dimensions: String,
    val altDescription: String,
    val likes: String,
    val views: String,
    val downloads: String,
    val imageFull: String,
    val imageRegular: String,
    val username: String,
    val name: String,
    val userProfileImage: String,
    val userLink: String,
    val exifModel: String,
    val exifExposureTime: String,
    val exifAperture: String,
    val exifFocalLength: String,
    val exifIso: String,
    val tags: List<String>
)