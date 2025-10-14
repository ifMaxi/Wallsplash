package com.maxidev.wallsplash.feature.photos.domain.repository

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.photos.domain.model.Collections
import com.maxidev.wallsplash.feature.photos.domain.model.Photos
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    fun fetchPhotos(): Flow<PagingData<Photos>>

    fun fetchCollections(): Flow<PagingData<Collections>>
}