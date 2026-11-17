package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.remote.dto.UserDto
import com.maxidev.wallsplash.domain.model.User

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