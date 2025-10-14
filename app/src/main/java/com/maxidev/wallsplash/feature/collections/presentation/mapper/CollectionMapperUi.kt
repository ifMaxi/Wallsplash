package com.maxidev.wallsplash.feature.collections.presentation.mapper

import com.maxidev.wallsplash.feature.collections.domain.model.CollectionData
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionPhotos
import com.maxidev.wallsplash.feature.collections.presentation.model.CollectionDataUi
import com.maxidev.wallsplash.feature.collections.presentation.model.CollectionPhotosUi

fun CollectionData.asUi() =
    CollectionDataUi(
        title = title,
        totalPhotos = "$totalPhotos photos",
        link = link,
        name = "Collection by $name"
    )

fun CollectionPhotos.asUi() =
    CollectionPhotosUi(
        photoId = photoId,
        id = id,
        width = width,
        height = height,
        color = color,
        urlRaw = urlRaw,
        urlFull = urlFull,
        urlRegular = urlRegular,
        urlSmall = urlSmall,
        urlThumb = urlThumb
    )