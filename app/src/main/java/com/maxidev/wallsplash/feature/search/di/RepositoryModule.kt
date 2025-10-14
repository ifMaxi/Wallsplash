package com.maxidev.wallsplash.feature.search.di

import com.maxidev.wallsplash.feature.search.data.repository.SearchRepositoryImpl
import com.maxidev.wallsplash.feature.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository
}