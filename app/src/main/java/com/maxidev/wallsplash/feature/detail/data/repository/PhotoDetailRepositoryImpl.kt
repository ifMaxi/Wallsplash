package com.maxidev.wallsplash.feature.detail.data.repository

import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.feature.detail.data.mapper.asDomain
import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail
import com.maxidev.wallsplash.feature.detail.domain.repository.PhotoDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PhotoDetailRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService
) : PhotoDetailRepository {

    override suspend fun fetchPhotoDetails(id: String): PhotoDetail {
        return withContext(Dispatchers.IO) {
            apiService.getPhotoId(id)
                .asDomain()
        }
    }
}