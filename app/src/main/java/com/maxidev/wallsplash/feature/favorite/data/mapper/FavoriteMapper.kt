package com.maxidev.wallsplash.feature.favorite.data.mapper

import com.maxidev.wallsplash.feature.favorite.data.local.entity.FavoriteEntity
import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites

fun FavoriteEntity.asDomain() =
    Favorites(
        id = id,
        photo = photo,
        width = width,
        height = height,
        blurHash = blurHash
    )

fun Favorites.asEntity() =
    FavoriteEntity(
        id = id,
        photo = photo,
        width = width,
        height = height,
        blurHash = blurHash
    )