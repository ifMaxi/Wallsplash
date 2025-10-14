package com.maxidev.wallsplash.feature.photos.domain.usecase

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.photos.domain.model.Photos
import com.maxidev.wallsplash.feature.photos.domain.repository.PhotosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AllPhotosUseCase @Inject constructor(
    private val repository: PhotosRepository
) {
    operator fun invoke(): Flow<PagingData<Photos>> =
        repository.fetchPhotos()
}