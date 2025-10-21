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
        blurHash = blurHash,
        urlRegular = urlRegular,
        name = name,
        profileImageLarge = profileImageLarge
    )