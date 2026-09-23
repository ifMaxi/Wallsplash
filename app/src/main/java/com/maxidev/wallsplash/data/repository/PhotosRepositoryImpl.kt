package com.maxidev.wallsplash.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.data.local.WallsplashDataBase
import com.maxidev.wallsplash.data.local.entity.PhotosEntity
import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.paging.PhotosRemoteMediator
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.domain.model.Photos
import com.maxidev.wallsplash.domain.repository.PhotosRepository
import com.maxidev.wallsplash.utils.Constants
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
            pageSize = Constants.PER_PAGE,
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
}