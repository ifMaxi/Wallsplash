package com.maxidev.wallsplash.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxidev.wallsplash.data.local.dao.CollectionsDao
import com.maxidev.wallsplash.data.local.entity.CollectionsEntity
import com.maxidev.wallsplash.data.local.dao.FavoritesDao
import com.maxidev.wallsplash.data.local.entity.FavoriteEntity
import com.maxidev.wallsplash.data.local.dao.PhotosDao
import com.maxidev.wallsplash.data.local.entity.PhotosEntity

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