package com.maxidev.wallsplash.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.wallsplash.utils.Constants

@Entity(tableName = Constants.COLLECTIONS_TABLE)
data class CollectionsEntity(
    @PrimaryKey(autoGenerate = true)
    val pageId: Int? = null,
    val id: String,
    val title: String,
    val totalPhotos: Int,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlRegular: String,
    val userId: String,
    val name: String,
    val profileImageLarge: String
)