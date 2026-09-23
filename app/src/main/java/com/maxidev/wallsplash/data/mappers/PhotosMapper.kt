package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.local.entity.PhotosEntity
import com.maxidev.wallsplash.data.remote.dto.PhotosDto
import com.maxidev.wallsplash.domain.model.Photos

fun PhotosDto.asEntity() =
    this.let { data ->
        PhotosEntity(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            blurHash = data.blurHash.orEmpty(),
            urlFull = data.urls?.full.orEmpty(),
            urlRegular = data.urls?.regular.orEmpty(),
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
        blurHash = blurHash,
        urlFull = urlFull,
        urlRegular = urlRegular,
        userId = userId,
        name = name,
        profileImageLarge = profileImageLarge
    )