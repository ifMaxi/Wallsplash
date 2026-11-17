package com.maxidev.wallsplash.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String? = "",
    val bio: String? = "",
    val location: String? = "",
    val links: LinksDto? = LinksDto(),
    @SerialName("profile_image")
    val profileImage: ProfileImageDto? = ProfileImageDto(),
    @SerialName("total_collections")
    val totalCollections: Int? = 0,
    @SerialName("total_photos")
    val totalPhotos: Int? = 0,
    val social: SocialDto? = SocialDto(),
    val photos: List<PhotoDto?>? = listOf(),
    val tags: TagsDto? = TagsDto()
) {
    @Serializable
    data class LinksDto(val html: String? = "")

    @Serializable
    data class ProfileImageDto(val large: String? = "")

    @Serializable
    data class SocialDto(
        @SerialName("instagram_username")
        val instagramUsername: String? = "",
        @SerialName("portfolio_url")
        val portfolioUrl: String? = "",
        @SerialName("twitter_username")
        val twitterUsername: String? = ""
    )

    @Serializable
    data class PhotoDto(
        val id: String? = "",
        @SerialName("blur_hash")
        val blurHash: String? = "",
        @SerialName("asset_type")
        val assetType: String? = "",
        val urls: UrlsDto? = UrlsDto()
    ) {
        @Serializable
        data class UrlsDto(val full: String? = "")
    }

    @Serializable
    data class TagsDto(val custom: List<CustomDto?>? = listOf()) {
        @Serializable
        data class CustomDto(val title: String? = "")
    }
}