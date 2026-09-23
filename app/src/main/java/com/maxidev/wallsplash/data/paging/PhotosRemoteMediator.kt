package com.maxidev.wallsplash.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.maxidev.wallsplash.data.local.WallsplashDataBase
import com.maxidev.wallsplash.data.local.entity.PhotosEntity
import com.maxidev.wallsplash.data.mappers.asEntity
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.data.remote.dto.PhotosDto
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val apiService: WallsplashApiService,
    private val dataBase: WallsplashDataBase
) : RemoteMediator<Int, PhotosEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotosEntity>
    ): MediatorResult {

        val page = when(loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()

                if (lastItem == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                lastItem.pageId
            }
        }

        return try {
            val dao = dataBase.photosDao()
            val pageSize = state.config.pageSize
            val response = apiService.getPhotos(
                page = page,
                perPage = pageSize
            )
            val endOfPaginationReached = response.size < pageSize

            dataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }

                dao.upsertAll(response.map(PhotosDto::asEntity))
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }
}