package com.maxidev.wallsplash.feature.collections.presentation.state

import androidx.paging.PagingData
import com.maxidev.wallsplash.common.utils.NetworkResourceUtil
import com.maxidev.wallsplash.feature.collections.presentation.model.CollectionDataUi
import com.maxidev.wallsplash.feature.collections.presentation.model.CollectionPhotosUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CollectionUiState(
    val collectionData: NetworkResourceUtil<CollectionDataUi> = NetworkResourceUtil.Loading(),
    val collectionPhoto: Flow<PagingData<CollectionPhotosUi>> = emptyFlow()
)