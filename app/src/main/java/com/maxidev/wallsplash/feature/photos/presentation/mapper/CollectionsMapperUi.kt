package com.maxidev.wallsplash.feature.photos.presentation.mapper

import com.maxidev.wallsplash.feature.photos.domain.model.Collections
import com.maxidev.wallsplash.feature.photos.presentation.model.CollectionsUi

fun Collections.asUi() =
    CollectionsUi(
        id = id,
        title = title,
        totalPhotos = "Total photos: $totalPhotos",
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