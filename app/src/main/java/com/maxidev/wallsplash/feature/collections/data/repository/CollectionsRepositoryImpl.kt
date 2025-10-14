package com.maxidev.wallsplash.feature.collections.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.common.utils.Constants.PER_PAGE
import com.maxidev.wallsplash.feature.collections.data.mapper.asDomain
import com.maxidev.wallsplash.feature.collections.data.model.remote.CollectionPhotosDto
import com.maxidev.wallsplash.feature.collections.data.paging.CollectionPhotosPagingSource
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionData
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionPhotos
import com.maxidev.wallsplash.feature.collections.domain.repository.CollectionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService
) : CollectionsRepository {

    override suspend fun fetchCollectionData(id: String): CollectionData {

        return withContext(Dispatchers.IO) {
            apiService.getCollectionId(id)
                .asDomain()
        }
    }

    override fun fetchCollectionPhotos(id: String): Flow<PagingData<CollectionPhotos>> {
        val sourceFactory = { CollectionPhotosPagingSource(apiService = apiService, id = id) }
        val pagingConfig = PagingConfig(
            pageSize = PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = sourceFactory
        ).flow
            .map { pagingData -> pagingData.map(CollectionPhotosDto::asDomain) }
            .flowOn(Dispatchers.IO)
    }
}