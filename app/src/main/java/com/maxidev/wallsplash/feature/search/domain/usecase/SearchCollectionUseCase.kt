package com.maxidev.wallsplash.feature.search.domain.usecase

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.search.domain.model.SearchCollections
import com.maxidev.wallsplash.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCollectionUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String): Flow<PagingData<SearchCollections>> =
    repository.fetchSearchCollections(query)
}