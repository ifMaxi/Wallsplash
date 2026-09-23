package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.remote.dto.CollectionDataDto
import com.maxidev.wallsplash.data.remote.dto.CollectionPhotosDto
import com.maxidev.wallsplash.domain.model.CollectionData
import com.maxidev.wallsplash.domain.model.CollectionPhotos

fun CollectionDataDto.asDomain() =
    this.let { data ->
        CollectionData(
            title = data.title.orEmpty(),
            totalPhotos = data.totalPhotos ?: 0,
            link = data.links?.html.orEmpty(),
            name = data.user?.name.orEmpty()
        )
    }

fun CollectionPhotosDto.asDomain() =
    this.let { data ->
        CollectionPhotos(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            blurHash = data.blurHash.orEmpty(),
            urlFull = data.urls?.full.orEmpty(),
            urlRegular = data.urls?.regular.orEmpty()
        )
    }