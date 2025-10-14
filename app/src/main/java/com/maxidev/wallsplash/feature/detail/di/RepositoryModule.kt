package com.maxidev.wallsplash.feature.detail.di

import com.maxidev.wallsplash.feature.detail.data.repository.PhotoDetailRepositoryImpl
import com.maxidev.wallsplash.feature.detail.domain.repository.PhotoDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPhotoDetailRepository(impl: PhotoDetailRepositoryImpl): PhotoDetailRepository
}