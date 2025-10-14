package com.maxidev.wallsplash.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxidev.wallsplash.feature.favorite.data.local.dao.FavoritesDao
import com.maxidev.wallsplash.feature.favorite.data.local.entity.FavoriteEntity
import com.maxidev.wallsplash.feature.photos.data.db.CollectionsDao
import com.maxidev.wallsplash.feature.photos.data.db.PhotosDao
import com.maxidev.wallsplash.feature.photos.data.model.local.CollectionsEntity
import com.maxidev.wallsplash.feature.photos.data.model.local.PhotosEntity

@Database(
    entities = [
        PhotosEntity::class,
        CollectionsEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WallsplashDataBase: RoomDatabase() {

    abstract fun photosDao(): PhotosDao
    abstract fun collectionsDao(): CollectionsDao
    abstract fun favoriteDao(): FavoritesDao
}