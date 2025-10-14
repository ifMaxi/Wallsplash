package com.maxidev.wallsplash.feature.search.data.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotosDto(val results: List<Result?>? = listOf()) {
    @Serializable
    data class Result(
        val id: String? = "",
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
}