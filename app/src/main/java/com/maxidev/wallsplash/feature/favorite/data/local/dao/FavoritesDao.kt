package com.maxidev.wallsplash.feature.favorite.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maxidev.wallsplash.feature.favorite.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface FavoritesDao {

    /**
     * Select all items in the database.
     */
    @Query("SELECT * FROM favorite_table")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    /**
     * Deletes one or more items in the database.
     */
    @Query("DELETE FROM favorite_table WHERE id IN (:id)")
    suspend fun deletePhotoById(id: List<UUID>)

    /**
     * Inserts an item into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: FavoriteEntity)
}