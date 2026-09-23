package com.maxidev.wallsplash.presentation.photos

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.Photos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PhotosUiState(
    val photos: Flow<PagingData<Photos>> = emptyFlow()
)