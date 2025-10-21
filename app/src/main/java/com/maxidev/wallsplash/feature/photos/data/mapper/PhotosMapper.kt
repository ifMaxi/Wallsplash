package com.maxidev.wallsplash.feature.photos.data.mapper

import com.maxidev.wallsplash.feature.photos.data.model.local.PhotosEntity
import com.maxidev.wallsplash.feature.photos.data.model.remote.PhotosDto
import com.maxidev.wallsplash.feature.photos.domain.model.Photos

fun PhotosDto.asEntity() =
    this.let { data ->
        PhotosEntity(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            blurHash = data.blurHash.orEmpty(),
            urlFull = data.urls?.full.orEmpty(),
            urlRegular = data.urls?.regular.orEmpty(),
            name = data.user?.name.orEmpty(),
            profileImageLarge = data.user?.profileImage?.large.orEmpty()
        )
    }

fun PhotosEntity.asDomain() =
    Photos(
        id = id,
        width = width,
        height = height,
        blurHash = blurHash,
        urlFull = urlFull,
        urlRegular = urlRegular,
        name = name,
        profileImageLarge = profileImageLarge
    )