package com.maxidev.wallsplash.feature.photos.presentation.mapper

import com.maxidev.wallsplash.feature.photos.domain.model.Photos
import com.maxidev.wallsplash.feature.photos.presentation.model.PhotosUi

fun Photos.toUi() =
    PhotosUi(
        id = id,
        width = width,
        height = height,
        blurHash = blurHash,
        urlFull = urlFull,
        urlRegular = urlRegular,
        name = name,
        profileImageLarge = profileImageLarge
    )