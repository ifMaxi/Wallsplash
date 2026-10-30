package com.maxidev.wallsplash.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetailDto(
    val id: String? = "",
    @SerialName("created_at")
    val createdAt: String? = "",
    @SerialName("updated_at")
    val updatedAt: String? = "",
    val width: Int? = 0,
    val height: Int? = 0,
    val color: String? = "",
    @SerialName("blur_hash")
    val blurHash: String? = "",
    val urls: Urls? = Urls(),
    @SerialName("alt_description")
    val altDescription: String? = "",
    val user: User? = User(),
    val exif: Exif? = Exif(),
    val location: Location? = Location(),
    val tags: List<Tag?>? = listOf()
) {
    @Serializable
    data class Urls(val full: String? = "")

    @Serializable
    data class User(
        val id: String? = "",
        val username: String? = "",
        val name: String? = "",
        @SerialName("profile_image")
        val profileImage: ProfileImage? = ProfileImage(),
    ) {
        @Serializable
        data class ProfileImage(val large: String? = "")
    }

    @Serializable
    data class Exif(
        val make: String? = "",
        val model: String? = "",
        val name: String? = "",
        @SerialName("exposure_time")
        val exposureTime: String? = "",
        val aperture: String? = "",
        @SerialName("focal_length")
        val focalLength: String? = "",
        val iso: Int? = 0
    )

    @Serializable
    data class Location(val name: String? = "")

    @Serializable
    data class Tag(val title: String? = "")
}