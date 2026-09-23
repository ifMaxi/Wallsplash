package com.maxidev.wallsplash.presentation.search

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.SearchCollections
import com.maxidev.wallsplash.domain.model.SearchPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val searchPhotos: Flow<PagingData<SearchPhoto>> = emptyFlow(),
    val searchCollections: Flow<PagingData<SearchCollections>> = emptyFlow()
)