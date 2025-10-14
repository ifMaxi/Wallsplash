package com.maxidev.wallsplash.feature.detail.domain.usecase

import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail
import com.maxidev.wallsplash.feature.detail.domain.repository.PhotoDetailRepository
import javax.inject.Inject

class GetPhotoDetailUseCase @Inject constructor(
    private val repository: PhotoDetailRepository
) {

    suspend operator fun invoke(id: String): PhotoDetail {
        return repository.fetchPhotoDetails(id)
    }
}