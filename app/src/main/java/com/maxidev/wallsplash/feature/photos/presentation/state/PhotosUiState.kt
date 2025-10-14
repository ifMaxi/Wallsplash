package com.maxidev.wallsplash.feature.photos.presentation.state

import androidx.paging.PagingData
import com.maxidev.wallsplash.feature.photos.presentation.model.CollectionsUi
import com.maxidev.wallsplash.feature.photos.presentation.model.PhotosUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PhotosUiState(
    val photos: Flow<PagingData<PhotosUi>> = emptyFlow(),
    val collections: Flow<PagingData<CollectionsUi>> = emptyFlow(),
    val isRefreshing: Boolean = false
)