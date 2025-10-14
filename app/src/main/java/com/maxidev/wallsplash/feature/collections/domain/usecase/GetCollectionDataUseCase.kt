package com.maxidev.wallsplash.feature.collections.domain.usecase

import com.maxidev.wallsplash.feature.collections.domain.model.CollectionData
import com.maxidev.wallsplash.feature.collections.domain.repository.CollectionsRepository
import javax.inject.Inject

class GetCollectionDataUseCase @Inject constructor(
    private val repository: CollectionsRepository
) {
    suspend operator fun invoke(id: String): CollectionData =
        repository.fetchCollectionData(id)
}