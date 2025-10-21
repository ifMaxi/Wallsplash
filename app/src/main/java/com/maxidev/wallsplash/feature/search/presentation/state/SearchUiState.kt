package com.maxidev.wallsplash.feature.search.presentation.state

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.search.presentation.model.SearchCollectionUi
import com.maxidev.wallsplash.feature.search.presentation.model.SearchPhotoUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val searchPhotos: Flow<PagingData<SearchPhotoUi>> = emptyFlow(),
    val searchCollections: Flow<PagingData<SearchCollectionUi>> = emptyFlow()
)