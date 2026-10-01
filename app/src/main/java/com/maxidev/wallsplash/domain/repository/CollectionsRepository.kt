package com.maxidev.wallsplash.domain.repository

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.Collections
import kotlinx.coroutines.flow.Flow

interface CollectionsRepository {

    fun fetchCollections(): Flow<PagingData<Collections>>
}