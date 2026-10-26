package com.maxidev.wallsplash.domain.model

data class PhotoDetail(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val width: Int,
    val height: Int,
    val aspectRatio: String,
    val color: String,
    val blurHash: String,
    val altDescription: String,

    // Url image
    val imageFull: String,

    // User
    val userId: String,
    val username: String,
    val name: String,
    val userProfileImage: String,

    // Camera exif
    val exifMake: String,
    val exifName: String,
    val exifModel: String,
    val exifExposureTime: String,
    val exifAperture: String,
    val exifFocalLength: String,
    val exifIso: String,

    // Location
    val locationName: String,
    val city: String,
    val country: String,

    // Tags
    val tags: List<String>
)