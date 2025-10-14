package com.maxidev.wallsplash.feature.collections.data.mapper

import com.maxidev.wallsplash.feature.collections.data.model.remote.CollectionDataDto
import com.maxidev.wallsplash.feature.collections.data.model.remote.CollectionPhotosDto
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionData
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionPhotos

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
            color = data.color.orEmpty(),
            urlRaw = data.urls?.raw.orEmpty(),
            urlFull = data.urls?.full.orEmpty(),
            urlRegular = data.urls?.regular.orEmpty(),
            urlSmall = data.urls?.small.orEmpty(),
            urlThumb = data.urls?.thumb.orEmpty()
        )
    }