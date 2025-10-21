package com.maxidev.wallsplash.feature.search.presentation.mapper

import com.maxidev.wallsplash.feature.search.domain.model.SearchCollections
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto
import com.maxidev.wallsplash.feature.search.presentation.model.SearchCollectionUi
import com.maxidev.wallsplash.feature.search.presentation.model.SearchPhotoUi

fun SearchPhoto.asUi() =
    SearchPhotoUi(
        photoId = photoId,
        id = id,
        width = width,
        height = height,
        blurHash = blurHash,
        urlRegular = urlRegular
    )

fun SearchCollections.asUi() =
    SearchCollectionUi(
        collectionId = collectionId,
        id = id,
        title = title,
        totalPhotos = "Total photos: $totalPhotos",
        width = width,
        height = height,
        blurHash = blurHash,
        coverPhoto = coverPhoto,
    )