package com.maxidev.wallsplash.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.maxidev.wallsplash.data.local.entity.PhotosEntity

@Dao
interface PhotosDao {

    @Query("SELECT * FROM photos_table")
    fun getAllPhotos(): PagingSource<Int, PhotosEntity>

    @Upsert
    suspend fun upsertAll(photos: List<PhotosEntity>)

    @Query("DELETE FROM photos_table")
    suspend fun clearAll()
}