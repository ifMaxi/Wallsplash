package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.remote.dto.UserCollectionsDto
import com.maxidev.wallsplash.data.remote.dto.UserDto
import com.maxidev.wallsplash.data.remote.dto.UserPhotosDto
import com.maxidev.wallsplash.domain.model.User
import com.maxidev.wallsplash.domain.model.UserCollections
import com.maxidev.wallsplash.domain.model.UserPhoto

fun UserDto.asDomain() =
    this.let { data ->
        User(
            name = data.name.orEmpty(),
            bio = data.bio.orEmpty(),
            location = data.location.orEmpty(),
            userHtml = data.links?.html.orEmpty(),
            profileImage = data.profileImage?.large.orEmpty(),
            totalCollections = "${data.totalCollections}",
            totalPhotos = "${data.totalPhotos}",
            instagramUsername = data.social?.instagramUsername.orEmpty(),
            twitterUsername = data.social?.twitterUsername.orEmpty(),
            portfolioUrl = data.social?.portfolioUrl.orEmpty(),
            photos = data.photos?.map { photo ->
                User.Photos(
                    id = photo?.id.orEmpty(),
                    blurHash = photo?.blurHash.orEmpty(),
                    assetType = photo?.assetType.orEmpty(),
                    imageUrl = photo?.urls?.full.orEmpty()
                )
            }.orEmpty(),
            tags = data.tags?.custom.let {
                User.Tags(
                    tag = it?.map { tag -> tag?.title.orEmpty() }.orEmpty()
                )
            }
        )
    }

fun UserPhotosDto.asDomain() =
    this.let { data ->
        UserPhoto(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            blurHash = data.blurHash.orEmpty(),
            urlRegular = data.urls?.regular.orEmpty()
        )
    }

fun UserCollectionsDto.asDomain() =
    this.let { data ->
        UserCollections(
            id = data.id.orEmpty(),
            title = data.title.orEmpty(),
            totalPhotos = "${data.totalPhotos}",
            width = data.coverPhoto?.width ?: 0,
            height = data.coverPhoto?.height ?: 0,
            blurHash = data.coverPhoto?.blurHash.orEmpty(),
            coverPhoto = data.coverPhoto?.urls?.regular.orEmpty()
        )
    }