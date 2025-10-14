package com.maxidev.wallsplash.feature.collections.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDataDto(
    val title: String? = "",
    @SerialName("total_photos")
    val totalPhotos: Int? = 0,
    val links: Links? = Links(),
    val user: User? = User()
) {
    @Serializable
    data class Links(val html: String? = "")

    @Serializable
    data class User(val name: String? = "")
}