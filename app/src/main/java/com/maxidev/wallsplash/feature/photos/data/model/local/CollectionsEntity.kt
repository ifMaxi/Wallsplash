package com.maxidev.wallsplash.feature.photos.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.wallsplash.common.utils.Constants.COLLECTIONS_TABLE

@Entity(tableName = COLLECTIONS_TABLE)
data class CollectionsEntity(
    @PrimaryKey(autoGenerate = true)
    val pageId: Int? = null,
    val id: String,
    val title: String,
    val totalPhotos: Int,
    val width: Int,
    val height: Int,
    val color: String,
    val blurHash: String,
    val urlRaw: String,
    val urlFull: String,
    val urlRegular: String,
    val urlSmall: String,
    val urlThumb: String,
    val userId: String,
    val name: String,
    val profileImageLarge: String
)