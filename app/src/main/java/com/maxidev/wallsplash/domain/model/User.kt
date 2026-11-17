package com.maxidev.wallsplash.domain.model

data class User(
    val name: String,
    val bio: String,
    val location: String,
    val userHtml: String,
    val profileImage: String,
    val totalCollections: String,
    val totalPhotos: String,
    val instagramUsername: String,
    val twitterUsername: String,
    val portfolioUrl: String,
    val photos: List<Photos>,
    val tags: Tags
) {
    data class Photos(
        val id: String,
        val blurHash: String,
        val assetType: String,
        val imageUrl: String
    )
    data class Tags(val tag: List<String>)
}