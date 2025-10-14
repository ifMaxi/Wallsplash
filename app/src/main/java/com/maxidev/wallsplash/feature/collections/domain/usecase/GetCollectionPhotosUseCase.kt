package com.maxidev.wallsplash.feature.collections.domain.usecase

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionPhotos
import com.maxidev.wallsplash.feature.collections.domain.repository.CollectionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCollectionPhotosUseCase @Inject constructor(
    private val repository: CollectionsRepository
) {
    operator fun invoke(id: String): Flow<PagingData<CollectionPhotos>> =
        repository.fetchCollectionPhotos(id)
}