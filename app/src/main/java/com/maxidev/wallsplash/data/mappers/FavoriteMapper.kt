package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.local.entity.FavoriteEntity
import com.maxidev.wallsplash.domain.model.Favorites

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