package com.maxidev.wallsplash.feature.favorite.domain.repository

import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface FavoriteRepository {

    fun fetchFavorites(): Flow<List<Favorites>>

    suspend fun saveToFavorite(photo: Favorites)

    suspend fun deleteSelectedPhotos(photos: List<UUID>)
}