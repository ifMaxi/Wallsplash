package com.maxidev.wallsplash.feature.favorite.domain.usecase

import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites
import com.maxidev.wallsplash.feature.favorite.domain.repository.FavoriteRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(photo: Favorites) =
        repository.deleteFromFavorites(photo)
}