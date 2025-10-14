package com.maxidev.wallsplash.feature.favorite.domain.usecase

import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites
import com.maxidev.wallsplash.feature.favorite.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    operator fun invoke(): Flow<List<Favorites>> =
        repository.fetchFavorites()
}