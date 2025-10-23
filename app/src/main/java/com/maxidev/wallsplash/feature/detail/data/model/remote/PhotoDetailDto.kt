package com.maxidev.wallsplash.feature.detail.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetailDto(
    val id: String? = "",
    val width: Int? = 0,
    val height: Int? = 0,
    @SerialName("blur_hash")
    val blurHash: String? = "",
    @SerialName("alt_description")
    val altDescription: String? = "",
    val urls: UrlsDto? = UrlsDto(),
    val likes: Int? = 0,
    val views: Int? = 0,
    val downloads: Int? = 0,
    val user: UserDto? = UserDto(),
    val exif: ExifDto? = ExifDto(),
    val tags: List<TagDto?>? = listOf()
) {
    @Serializable
    data class UrlsDto(
        val full: String? = "",
        val regular: String? = "",
    )

    @Serializable
    data class UserDto(
        val username: String? = "",
        val name: String? = "",
        val links: UserLinksDto? = UserLinksDto(),
        @SerialName("profile_image")
        val profileImage: ProfileImageDto? = ProfileImageDto()
    ) {
        @Serializable
        data class UserLinksDto(val html: String? = "")

        @Serializable
        data class ProfileImageDto(val large: String? = "")
    }

    @Serializable
    data class ExifDto(
        val model: String? = "",
        @SerialName("exposure_time")
        val exposureTime: String? = "",
        val aperture: String? = "",
        @SerialName("focal_length")
        val focalLength: String? = "",
        val iso: Int? = 0
    )

    @Serializable
    data class TagDto(val title: String? = "")
}