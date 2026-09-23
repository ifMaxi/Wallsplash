package com.maxidev.wallsplash.data.repository

import com.maxidev.wallsplash.data.local.dao.FavoritesDao
import com.maxidev.wallsplash.data.local.entity.FavoriteEntity
import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.mappers.asEntity
import com.maxidev.wallsplash.domain.model.Favorites
import com.maxidev.wallsplash.domain.repository.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoritesDao
) : FavoriteRepository {

    override fun fetchFavorites(): Flow<List<Favorites>> {

        return dao.getFavorites()
            .map { favorites -> favorites.map(FavoriteEntity::asDomain) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun saveToFavorite(photo: Favorites) {

        return withContext(Dispatchers.IO) {
            dao.insertPhoto(photo.asEntity())
        }
    }

    override suspend fun deleteSelectedPhotos(photos: List<UUID>) {

        return withContext(Dispatchers.IO) {
            dao.deletePhotoById(photos)
        }
    }
}