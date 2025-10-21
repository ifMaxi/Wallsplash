package com.maxidev.wallsplash.feature.collections.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionPhotosDto(
    val id: String? = "",
    val width: Int? = 0,
    val height: Int? = 0,
    @SerialName("blur_hash")
    val blurHash: String? = "",
    val urls: Urls? = Urls()
) {
    @Serializable
    data class Urls(
        val full: String? = "",
        val regular: String? = ""
    )
}