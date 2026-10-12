package com.maxidev.wallsplash.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.maxidev.wallsplash.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Query(
        """
            SELECT * FROM history_table
            WHERE `query` LIKE '%' || :filter || '%'
            ORDER BY timeStamp DESC
            LIMIT :limit
        """
    )
    fun filterHistory(filter: String, limit: Int = 50): Flow<List<HistoryEntity>>

    @Upsert
    suspend fun upsert(entity: HistoryEntity)

    @Query("DELETE FROM history_table WHERE `query` = :query")
    suspend fun delete(query: String)

    @Query("DELETE FROM history_table")
    suspend fun clear()
}