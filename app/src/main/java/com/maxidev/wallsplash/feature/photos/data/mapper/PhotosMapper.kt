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
            color = data.color.orEmpty(),
            blurHash = data.blurHash.orEmpty(),
            urlRaw = data.urls?.raw.orEmpty(),
            urlFull = data.urls?.full.orEmpty(),
            urlRegular = data.urls?.regular.orEmpty(),
            urlSmall = data.urls?.small.orEmpty(),
            urlThumb = data.urls?.thumb.orEmpty(),
            userId = data.user?.id.orEmpty(),
            name = data.user?.name.orEmpty(),
            profileImageLarge = data.user?.profileImage?.large.orEmpty()
        )
    }

fun PhotosEntity.asDomain() =
    Photos(
        id = id,
        width = width,
        height = height,
        color = color,
        blurHash = blurHash,
        urlRaw = urlRaw,
        urlFull = urlFull,
        urlRegular = urlRegular,
        urlSmall = urlSmall,
        urlThumb = urlThumb,
        userId = userId,
        name = name,
        profileImageLarge = profileImageLarge
    )