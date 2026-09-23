package com.maxidev.wallsplash.presentation.collections

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.Collections
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CollectionsUiState(
    val collections: Flow<PagingData<Collections>> = emptyFlow()
)