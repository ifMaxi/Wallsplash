package com.maxidev.wallsplash.feature.search.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchCollectionsDto(val results: List<Result?>? = listOf()) {
    @Serializable
    data class Result(
        val id: String? = "",
        val title: String? = "",
        @SerialName("total_photos")
        val totalPhotos: Int? = 0,
        @SerialName("cover_photo")
        val coverPhoto: CoverPhoto? = CoverPhoto()
    ) {
        @Serializable
        data class CoverPhoto(
            val width: Int = 0,
            val height: Int = 0,
            val blurHash: String = "",
            val urls: Urls? = Urls()
        ) {
            @Serializable
            data class Urls(val regular: String? = "")
        }
    }
}