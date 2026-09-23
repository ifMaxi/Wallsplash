package com.maxidev.wallsplash.di

import com.maxidev.wallsplash.data.repository.CollectionsRepositoryImpl
import com.maxidev.wallsplash.data.repository.FavoriteRepositoryImpl
import com.maxidev.wallsplash.data.repository.DetailRepositoryImpl
import com.maxidev.wallsplash.data.repository.PhotosRepositoryImpl
import com.maxidev.wallsplash.data.repository.SearchRepositoryImpl
import com.maxidev.wallsplash.domain.repository.CollectionsRepository
import com.maxidev.wallsplash.domain.repository.FavoriteRepository
import com.maxidev.wallsplash.domain.repository.DetailRepository
import com.maxidev.wallsplash.domain.repository.PhotosRepository
import com.maxidev.wallsplash.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository

    @Binds
    abstract fun bindsCollectionsRepository(
        impl: CollectionsRepositoryImpl
    ): CollectionsRepository

    @Binds
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindFavoritesRepository(
        impl: FavoriteRepositoryImpl
    ): FavoriteRepository

    @Binds
    abstract fun bindPhotoDetailRepository(
        impl: DetailRepositoryImpl
    ): DetailRepository
}