package com.maxidev.wallsplash.feature.photos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.common.data.local.WallsplashDataBase
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.common.utils.Constants.PER_PAGE
import com.maxidev.wallsplash.feature.photos.data.mapper.asDomain
import com.maxidev.wallsplash.feature.photos.data.model.local.CollectionsEntity
import com.maxidev.wallsplash.feature.photos.data.model.local.PhotosEntity
import com.maxidev.wallsplash.feature.photos.data.paging.CollectionsRemoteMediator
import com.maxidev.wallsplash.feature.photos.data.paging.PhotosRemoteMediator
import com.maxidev.wallsplash.feature.photos.domain.model.Collections
import com.maxidev.wallsplash.feature.photos.domain.model.Photos
import com.maxidev.wallsplash.feature.photos.domain.repository.PhotosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PhotosRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService,
    private val dataBase: WallsplashDataBase
): PhotosRepository {

    override fun fetchPhotos(): Flow<PagingData<Photos>> {
        val pagingSourceFactory = { dataBase.photosDao().getAllPhotos() }
        val remoteMediator = PhotosRemoteMediator(apiService = apiService, dataBase = dataBase)
        val pagingConfig = PagingConfig(
            pageSize = PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map(PhotosEntity::asDomain) }
            .flowOn(Dispatchers.IO)
    }

    override fun fetchCollections(): Flow<PagingData<Collections>> {
        val pagingSourceFactory = { dataBase.collectionsDao().getAllCollections() }
        val remoteMediator = CollectionsRemoteMediator(api = apiService, database = dataBase)
        val pagingConfig = PagingConfig(
            pageSize = PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData -> pagingData.map(CollectionsEntity::asDomain) }
            .flowOn(Dispatchers.IO)
    }
}