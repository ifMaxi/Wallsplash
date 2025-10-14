package com.maxidev.wallsplash.feature.photos.presentation.mapper

import com.maxidev.wallsplash.feature.photos.domain.model.Photos
import com.maxidev.wallsplash.feature.photos.presentation.model.PhotosUi

fun Photos.toUi() =
    PhotosUi(
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