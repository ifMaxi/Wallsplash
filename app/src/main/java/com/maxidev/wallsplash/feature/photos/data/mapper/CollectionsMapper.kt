package com.maxidev.wallsplash.feature.photos.data.mapper

import com.maxidev.wallsplash.feature.photos.data.model.local.CollectionsEntity
import com.maxidev.wallsplash.feature.photos.data.model.remote.CollectionsDto
import com.maxidev.wallsplash.feature.photos.domain.model.Collections

fun CollectionsDto.asEntity() =
    this.let { data ->
        CollectionsEntity(
            id = data.id.orEmpty(),
            title = data.title.orEmpty(),
            totalPhotos = data.totalPhotos ?: 0,
            width = data.coverPhoto?.width ?: 0,
            height = data.coverPhoto?.height ?: 0,
            color = data.coverPhoto?.color.orEmpty(),
            blurHash = data.coverPhoto?.blurHash.orEmpty(),
            urlRaw = data.coverPhoto?.urls?.raw.orEmpty(),
            urlFull = data.coverPhoto?.urls?.full.orEmpty(),
            urlRegular = data.coverPhoto?.urls?.regular.orEmpty(),
            urlSmall = data.coverPhoto?.urls?.small.orEmpty(),
            urlThumb = data.coverPhoto?.urls?.thumb.orEmpty(),
            userId = data.user?.id.orEmpty(),
            name = data.user?.name.orEmpty(),
            profileImageLarge = data.user?.profileImage?.large.orEmpty()
        )
    }

fun CollectionsEntity.asDomain() =
    Collections(
        id = id,
        title = title,
        totalPhotos = totalPhotos,
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