package com.maxidev.wallsplash.feature.photos.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotosDto(
    val id: String? = "",
    val width: Int? = 0,
    val height: Int? = 0,
    @SerialName("blur_hash")
    val blurHash: String? = "",
    val user: User? = User(),
    val urls: Urls? = Urls()
) {
    @Serializable
    data class User(
        val name: String? = "",
        @SerialName("profile_image")
        val profileImage: ProfileImage? = ProfileImage()
    ) {
        @Serializable
        data class ProfileImage(val large: String? = "")
    }

    @Serializable
    data class Urls(
        val full: String? = "",
        val regular: String? = ""
    )
}