package com.maxidev.wallsplash.domain.repository

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.Photos
import kotlinx.coroutines.flow.Flow

interface PhotosRepository {

    fun fetchPhotos(): Flow<PagingData<Photos>>
}