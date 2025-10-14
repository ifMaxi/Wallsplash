package com.maxidev.wallsplash.feature.collections.di

import com.maxidev.wallsplash.feature.collections.data.repository.CollectionsRepositoryImpl
import com.maxidev.wallsplash.feature.collections.domain.repository.CollectionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsCollectionRepository(
        impl: CollectionsRepositoryImpl
    ): CollectionsRepository
}