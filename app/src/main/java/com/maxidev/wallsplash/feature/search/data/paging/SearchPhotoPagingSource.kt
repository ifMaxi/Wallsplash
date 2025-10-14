package com.maxidev.wallsplash.feature.search.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.common.utils.Constants.PAGE_NUMBER
import com.maxidev.wallsplash.feature.search.data.mapper.asDomain
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto
import okio.IOException
import retrofit2.HttpException

class SearchPhotoPagingSource(
    private val apiService: WallsplashApiService,
    private val query: String
) : PagingSource<Int, SearchPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, SearchPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchPhoto> {
        return try {
            val page = params.key ?: PAGE_NUMBER
            val perPage = params.loadSize
            val response = apiService.getSearchPhotos(
                query = query,
                page = page,
                perPage = perPage
            )
            val mappedResponse = response.asDomain()
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (mappedResponse.isNullOrEmpty()) null else page + 1

            LoadResult.Page(
                data = mappedResponse.orEmpty(),
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}