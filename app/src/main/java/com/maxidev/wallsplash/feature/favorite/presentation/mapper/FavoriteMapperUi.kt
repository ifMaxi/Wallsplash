package com.maxidev.wallsplash.feature.favorite.presentation.mapper

import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi

fun Favorites.asUi() =
    FavoritesUi(
        photoId = id,
        photo = photo,
        width = width,
        height = height,
        blurHash = blurHash
    )

fun FavoritesUi.asDomain() =
    Favorites(
        id = photoId,
        photo = photo,
        width = width,
        height = height,
        blurHash = blurHash
    )