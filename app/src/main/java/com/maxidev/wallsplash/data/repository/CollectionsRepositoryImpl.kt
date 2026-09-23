package com.maxidev.wallsplash.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.data.local.WallsplashDataBase
import com.maxidev.wallsplash.data.local.entity.CollectionsEntity
import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.paging.CollectionsRemoteMediator
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.domain.model.Collections
import com.maxidev.wallsplash.domain.repository.CollectionsRepository
import com.maxidev.wallsplash.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService,
    private val dataBase: WallsplashDataBase
): CollectionsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun fetchCollections(): Flow<PagingData<Collections>> {
        val pagingSourceFactory = { dataBase.collectionsDao().getAllCollections() }
        val remoteMediator = CollectionsRemoteMediator(api = apiService, database = dataBase)
        val pagingConfig = PagingConfig(
            pageSize = Constants.PER_PAGE,
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