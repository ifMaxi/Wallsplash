package com.maxidev.wallsplash.feature.search.domain.usecase

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto
import com.maxidev.wallsplash.feature.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPhotoUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(query: String): Flow<PagingData<SearchPhoto>> =
        repository.fetchSearchPhotos(query)
}