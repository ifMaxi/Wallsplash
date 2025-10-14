package com.maxidev.wallsplash.feature.favorite.di

import com.maxidev.wallsplash.feature.favorite.data.repository.FavoriteRepositoryImpl
import com.maxidev.wallsplash.feature.favorite.domain.repository.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindFavoritesRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository
}