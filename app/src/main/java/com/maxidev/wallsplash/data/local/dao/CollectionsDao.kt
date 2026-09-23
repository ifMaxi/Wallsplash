package com.maxidev.wallsplash.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.maxidev.wallsplash.data.local.entity.CollectionsEntity

@Dao
interface CollectionsDao {

    @Query("SELECT * FROM collections_table")
    fun getAllCollections(): PagingSource<Int, CollectionsEntity>

    @Upsert
    suspend fun upsertAll(collections: List<CollectionsEntity>)

    @Query("DELETE FROM collections_table")
    suspend fun clearAll()
}