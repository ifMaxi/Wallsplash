package com.maxidev.wallsplash.feature.favorite.domain.usecase

import com.maxidev.wallsplash.feature.favorite.domain.repository.FavoriteRepository
import java.util.UUID
import javax.inject.Inject

class DeleteSelectedPhotosUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(photos: List<UUID>) =
        repository.deleteSelectedPhotos(photos)
}