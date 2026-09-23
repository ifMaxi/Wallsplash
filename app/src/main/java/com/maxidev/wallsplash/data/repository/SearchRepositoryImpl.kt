package com.maxidev.wallsplash.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.data.paging.SearchCollectionPagingSource
import com.maxidev.wallsplash.data.paging.SearchPhotoPagingSource
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.domain.model.SearchCollections
import com.maxidev.wallsplash.domain.model.SearchPhoto
import com.maxidev.wallsplash.domain.repository.SearchRepository
import com.maxidev.wallsplash.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService
) : SearchRepository {

    override fun fetchSearchPhotos(query: String): Flow<PagingData<SearchPhoto>> {
        val sourceFactory = { SearchPhotoPagingSource(apiService = apiService, query = query) }
        val pagingConfig = PagingConfig(
            pageSize = Constants.PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = sourceFactory
        ).flow
            .distinctUntilChangedBy { key -> key.map { it.photoId } }
            .flowOn(Dispatchers.IO)
    }

    override fun fetchSearchCollections(query: String): Flow<PagingData<SearchCollections>> {
        val sourceFactory = { SearchCollectionPagingSource(apiService = apiService, query = query) }
        val pagingConfig = PagingConfig(
            pageSize = Constants.PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = sourceFactory
        ).flow
            .distinctUntilChangedBy { key -> key.map { it.collectionId } }
            .flowOn(Dispatchers.IO)
    }
}