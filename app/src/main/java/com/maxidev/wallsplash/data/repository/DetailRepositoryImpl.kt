package com.maxidev.wallsplash.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.paging.CollectionPhotosPagingSource
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.data.remote.dto.CollectionPhotosDto
import com.maxidev.wallsplash.domain.model.CollectionData
import com.maxidev.wallsplash.domain.model.PhotoDetail
import com.maxidev.wallsplash.domain.repository.DetailRepository
import com.maxidev.wallsplash.domain.model.CollectionPhotos
import com.maxidev.wallsplash.utils.Constants.PER_PAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService
) : DetailRepository {

    override suspend fun fetchPhotoDetails(id: String): PhotoDetail {
        return withContext(Dispatchers.IO) {
            apiService.getPhotoId(id)
                .asDomain()
        }
    }

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