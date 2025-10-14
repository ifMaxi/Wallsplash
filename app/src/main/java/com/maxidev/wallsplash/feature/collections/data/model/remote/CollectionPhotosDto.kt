package com.maxidev.wallsplash.feature.collections.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class CollectionPhotosDto(
    val id: String? = "",
    val width: Int? = 0,
    val height: Int? = 0,
    val color: String? = "",
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