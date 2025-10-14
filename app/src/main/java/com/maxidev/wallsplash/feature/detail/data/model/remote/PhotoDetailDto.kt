package com.maxidev.wallsplash.feature.detail.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetailDto(
    val id: String? = "",
    val width: Int? = 0,
    val height: Int? = 0,
    val color: String? = "",
    @SerialName("blur_hash")
    val blurHash: String? = "",
    @SerialName("alt_description")
    val altDescription: String? = "",
    val urls: UrlsDto? = UrlsDto(),
    val likes: Int? = 0,
    val user: UserDto? = UserDto(),
    val exif: ExifDto? = ExifDto(),
    val location: LocationDto? = LocationDto(),
    val tags: List<TagDto?>? = listOf(),
    val views: Int? = 0,
    val downloads: Int? = 0
) {
    @Serializable
    data class UrlsDto(
        val raw: String? = "",
        val full: String? = "",
        val regular: String? = "",
        val small: String? = "",
        val thumb: String? = ""
    )

    @Serializable
    data class UserDto(
        val id: String? = "",
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
    data class LocationDto(
        val city: String? = "",
        val country: String? = "",
        val position: PositionDto? = PositionDto()
    ) {
        @Serializable
        data class PositionDto(
            val latitude: Double? = 0.0,
            val longitude: Double? = 0.0
        )
    }

    @Serializable
    data class TagDto(val title: String? = "")
}