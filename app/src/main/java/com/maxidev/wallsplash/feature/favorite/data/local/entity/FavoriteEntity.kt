package com.maxidev.wallsplash.feature.favorite.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.wallsplash.common.utils.Constants
import java.util.UUID

@Entity(tableName = Constants.FAVORITE_TABLE)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: UUID = UUID.randomUUID(),
    val photo: String,
    val width: Int,
    val height: Int,
    val blurHash: String
)