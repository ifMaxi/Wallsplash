package com.maxidev.wallsplash.data.remote.dto

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
        @SerialName("blur_hash")
        val blurHash: String? = "",
        val urls: Urls? = Urls()
    ) {
        @Serializable
        data class Urls(val regular: String? = "")
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