package com.maxidev.wallsplash.feature.favorite.data.repository

import com.maxidev.wallsplash.feature.favorite.data.local.dao.FavoritesDao
import com.maxidev.wallsplash.feature.favorite.data.local.entity.FavoriteEntity
import com.maxidev.wallsplash.feature.favorite.data.mapper.asDomain
import com.maxidev.wallsplash.feature.favorite.data.mapper.asEntity
import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites
import com.maxidev.wallsplash.feature.favorite.domain.repository.FavoriteRepository
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

    override suspend fun deleteFromFavorites(photo: Favorites) {

        return withContext(Dispatchers.IO) {
            dao.deletePhoto(photo.asEntity())
        }
    }

    override suspend fun deleteSelectedPhotos(photos: List<UUID>) {

        return withContext(Dispatchers.IO) {
            dao.deletePhotoById(photos)
        }
    }
}