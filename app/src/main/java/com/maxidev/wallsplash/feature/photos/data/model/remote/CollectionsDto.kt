package com.maxidev.wallsplash.feature.photos.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionsDto(
    val id: String? = "",
    val title: String? = "",
    @SerialName("total_photos")
    val totalPhotos: Int? = 0,
    @SerialName("cover_photo")
    val coverPhoto: CoverPhoto? = CoverPhoto(),
    val user: User? = User()
) {
    @Serializable
    data class CoverPhoto(
        val width: Int? = 0,
        val height: Int? = 0,
        val color: String? = "",
        @SerialName("blur_hash")
        val blurHash: String? = "",
        val urls: Urls? = Urls()
    ) {
        @Serializable
        data class Urls(
            val raw: String? = "",
            val full: String? = "",
            val regular: String? = "",
            val small: String? = "",
            val thumb: String? = ""
        )
    }
    @Serializable
    data class User(
        val id: String? = "",
        val name: String? = "",
        @SerialName("profile_image")
        val profileImage: ProfileImage? = ProfileImage()
    ) {
        @Serializable
        data class ProfileImage(val large: String? = "")
    }
}