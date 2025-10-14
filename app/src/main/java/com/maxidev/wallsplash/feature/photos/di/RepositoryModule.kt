package com.maxidev.wallsplash.feature.photos.di

import com.maxidev.wallsplash.feature.photos.data.repository.PhotosRepositoryImpl
import com.maxidev.wallsplash.feature.photos.domain.repository.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository
}