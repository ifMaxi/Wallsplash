package com.maxidev.wallsplash.feature.photos.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.wallsplash.common.utils.Constants.PHOTOS_TABLE

@Entity(tableName = PHOTOS_TABLE)
data class PhotosEntity(
    @PrimaryKey(autoGenerate = false)
    val pageId: Int? = null,
    val id: String,
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