package com.maxidev.wallsplash.feature.favorite.di

import com.maxidev.wallsplash.common.data.local.WallsplashDataBase
import com.maxidev.wallsplash.feature.favorite.data.local.dao.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providesFavoriteDao(dataBase: WallsplashDataBase): FavoritesDao =
        dataBase.favoriteDao()
}