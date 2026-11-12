package com.maxidev.wallsplash.presentation.detail.collection

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.CollectionData
import com.maxidev.wallsplash.domain.model.CollectionPhotos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CollectionUiState(
    val collectionData: CollectionData? = null,
    val collectionPhoto: Flow<PagingData<CollectionPhotos>> = emptyFlow(),
    val isLoading: Boolean = false,
    val isError: String? = null
)