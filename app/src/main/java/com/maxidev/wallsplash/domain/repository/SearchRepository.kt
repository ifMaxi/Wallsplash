package com.maxidev.wallsplash.domain.repository

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.History
import com.maxidev.wallsplash.domain.model.SearchCollections
import com.maxidev.wallsplash.domain.model.SearchPhoto
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun fetchSearchPhotos(query: String): Flow<PagingData<SearchPhoto>>

    fun fetchSearchCollections(query: String): Flow<PagingData<SearchCollections>>

    fun getHistory(filter: String): Flow<List<History>>

    suspend fun upsertHistory(entity: History)

    suspend fun deleteHistory(query: String)

    suspend fun deleteAllHistory()
}