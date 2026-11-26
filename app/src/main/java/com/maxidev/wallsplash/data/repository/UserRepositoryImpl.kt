package com.maxidev.wallsplash.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.paging.UserCollectionsPagingSource
import com.maxidev.wallsplash.data.paging.UserPhotosPagingSource
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.domain.model.User
import com.maxidev.wallsplash.domain.model.UserCollections
import com.maxidev.wallsplash.domain.model.UserPhoto
import com.maxidev.wallsplash.domain.repository.UserRepository
import com.maxidev.wallsplash.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService
) : UserRepository {

    override suspend fun fetchUser(id: String): User {
        return withContext(Dispatchers.IO) {
            apiService.getUserId(id)
                .asDomain()
        }
    }

    override fun fetchUserPhotos(id: String): Flow<PagingData<UserPhoto>> {
        val factory = { UserPhotosPagingSource(apiService = apiService, id = id) }
        val pagingConfig = PagingConfig(
            pageSize = Constants.PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = factory
        ).flow
            .flowOn(Dispatchers.IO)
    }

    override fun fetchUserCollections(id: String): Flow<PagingData<UserCollections>> {
        val factory = { UserCollectionsPagingSource(apiService = apiService, id = id) }
        val pagingConfig = PagingConfig(
            pageSize = Constants.PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = factory
        ).flow
            .flowOn(Dispatchers.IO)
    }
}