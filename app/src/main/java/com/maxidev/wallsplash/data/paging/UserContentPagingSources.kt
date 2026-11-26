package com.maxidev.wallsplash.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.data.remote.dto.UserCollectionsDto
import com.maxidev.wallsplash.data.remote.dto.UserPhotosDto
import com.maxidev.wallsplash.domain.model.UserCollections
import com.maxidev.wallsplash.domain.model.UserPhoto
import com.maxidev.wallsplash.utils.Constants.PAGE_NUMBER
import okio.IOException
import retrofit2.HttpException

class UserPhotosPagingSource(
    private val apiService: WallsplashApiService,
    private val id: String
) : PagingSource<Int, UserPhoto>() {

    override fun getRefreshKey(state: PagingState<Int, UserPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserPhoto> {
        return try {
            val page = params.key ?: PAGE_NUMBER
            val perPage = params.loadSize
            val response = apiService.getUserPhotos(username = id, page = page, perPage = perPage)
            val mappedResponse = response.map(UserPhotosDto::asDomain)
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (mappedResponse.isEmpty()) null else page + 1

            LoadResult.Page(
                data = mappedResponse,
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

class UserCollectionsPagingSource(
    private val apiService: WallsplashApiService,
    private val id: String
) : PagingSource<Int, UserCollections>() {

    override fun getRefreshKey(state: PagingState<Int, UserCollections>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserCollections> {
        return try {
            val page = params.key ?: PAGE_NUMBER
            val perPage = params.loadSize
            val response = apiService.getUserCollections(
                username = id, page = page, perPage = perPage
            )
            val mappedResponse = response.map(UserCollectionsDto::asDomain)
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (mappedResponse.isEmpty()) null else page + 1

            LoadResult.Page(
                data = mappedResponse,
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