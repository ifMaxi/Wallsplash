package com.maxidev.wallsplash.feature.collections.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.common.utils.Constants.PAGE_NUMBER
import com.maxidev.wallsplash.feature.collections.data.model.remote.CollectionPhotosDto
import okio.IOException
import retrofit2.HttpException

class CollectionPhotosPagingSource(
    private val apiService: WallsplashApiService,
    private val id: String
) : PagingSource<Int, CollectionPhotosDto>() {

    override fun getRefreshKey(state: PagingState<Int, CollectionPhotosDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionPhotosDto> {
        return try {
            val page = params.key ?: PAGE_NUMBER
            val perPage = params.loadSize
            val response = apiService.getCollectionPhotosById(
                id = id,
                page = page,
                perPage = perPage
            )
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (response.isEmpty()) null else page + 1

            LoadResult.Page(
                data = response,
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