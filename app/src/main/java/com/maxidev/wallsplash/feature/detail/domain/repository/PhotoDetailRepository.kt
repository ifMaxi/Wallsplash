package com.maxidev.wallsplash.feature.detail.domain.repository

import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail

interface PhotoDetailRepository {

    suspend fun fetchPhotoDetails(id: String): PhotoDetail
}