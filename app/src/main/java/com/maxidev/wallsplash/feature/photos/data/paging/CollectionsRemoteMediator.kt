package com.maxidev.wallsplash.feature.photos.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.maxidev.wallsplash.common.data.local.WallsplashDataBase
import com.maxidev.wallsplash.common.data.remote.WallsplashApiService
import com.maxidev.wallsplash.feature.photos.data.mapper.asEntity
import com.maxidev.wallsplash.feature.photos.data.model.local.CollectionsEntity
import com.maxidev.wallsplash.feature.photos.data.model.remote.CollectionsDto
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class CollectionsRemoteMediator(
    private val api: WallsplashApiService,
    private val database: WallsplashDataBase
) : RemoteMediator<Int, CollectionsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CollectionsEntity>
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
            val dao = database.collectionsDao()
            val pageSize = state.config.pageSize
            val response = api.getCollections(
                page = page,
                perPage = pageSize
            )
            val endOfPaginationReached = response.size < pageSize

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearAll()
                }

                dao.upsertAll(response.map(CollectionsDto::asEntity))
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }
}