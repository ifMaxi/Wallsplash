package com.maxidev.wallsplash.presentation.detail.collection

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.CollectionData
import com.maxidev.wallsplash.domain.model.CollectionPhotos
import com.maxidev.wallsplash.utils.NetworkResourceUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CollectionUiState(
    val collectionData: NetworkResourceUtil<CollectionData> = NetworkResourceUtil.Loading(),
    val collectionPhoto: Flow<PagingData<CollectionPhotos>> = emptyFlow()
)