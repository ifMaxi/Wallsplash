package com.maxidev.wallsplash.domain.repository

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.CollectionData
import com.maxidev.wallsplash.domain.model.CollectionPhotos
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {

    suspend fun fetchCollectionData(id: String): CollectionData

    fun fetchCollectionPhotos(id: String): Flow<PagingData<CollectionPhotos>>
}