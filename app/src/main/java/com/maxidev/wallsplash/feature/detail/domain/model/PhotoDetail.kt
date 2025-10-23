package com.maxidev.wallsplash.feature.detail.domain.model

data class PhotoDetail(
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val altDescription: String,
    val likes: Int,
    val views: Int,
    val downloads: Int,
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
    val exifIso: Int,
    val tags: List<String>
)