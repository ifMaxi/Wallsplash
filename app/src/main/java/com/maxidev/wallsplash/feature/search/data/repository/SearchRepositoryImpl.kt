package com.maxidev.wallsplash.feature.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.common.utils.Constants.PER_PAGE
import com.maxidev.wallsplash.feature.search.data.paging.SearchPhotoPagingSource
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto
import com.maxidev.wallsplash.feature.search.domain.repository.SearchRepository
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
            pageSize = PER_PAGE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = sourceFactory
        ).flow
            .distinctUntilChangedBy { key -> key.map { it.photoId } }
            .flowOn(Dispatchers.IO)
    }
}