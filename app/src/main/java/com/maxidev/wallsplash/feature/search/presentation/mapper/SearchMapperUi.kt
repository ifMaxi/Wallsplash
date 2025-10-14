package com.maxidev.wallsplash.feature.search.presentation.mapper

import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto
import com.maxidev.wallsplash.feature.search.presentation.model.SearchPhotoUi

fun SearchPhoto.asUi() =
    SearchPhotoUi(
        photoId = photoId,
        id = id,
        width = width,
        height = height,
        color = color,
        blurHash = blurHash,
        urlRaw = urlRaw,
        urlFull = urlFull,
        urlRegular = urlRegular,
        urlSmall = urlSmall,
        urlThumb = urlThumb
    )