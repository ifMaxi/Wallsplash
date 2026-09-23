package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.local.entity.CollectionsEntity
import com.maxidev.wallsplash.data.remote.dto.CollectionsDto
import com.maxidev.wallsplash.domain.model.Collections

fun CollectionsDto.asEntity() =
    this.let { data ->
        CollectionsEntity(
            id = data.id.orEmpty(),
            title = data.title.orEmpty(),
            totalPhotos = data.totalPhotos ?: 0,
            width = data.coverPhoto?.width ?: 0,
            height = data.coverPhoto?.height ?: 0,
            blurHash = data.coverPhoto?.blurHash.orEmpty(),
            urlRegular = data.coverPhoto?.urls?.regular.orEmpty(),
            userId = data.user?.id.orEmpty(),
            name = data.user?.name.orEmpty(),
            profileImageLarge = data.user?.profileImage?.large.orEmpty()
        )
    }

fun CollectionsEntity.asDomain() =
    Collections(
        id = id,
        title = title,
        totalPhotos = "$totalPhotos",
        width = width,
        height = height,
        blurHash = blurHash,
        urlRegular = urlRegular,
        userId = userId,
        name = name,
        profileImageLarge = profileImageLarge
    )