package com.maxidev.wallsplash.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.wallsplash.utils.Constants

@Entity(tableName = Constants.PHOTOS_TABLE)
data class PhotosEntity(
    @PrimaryKey(autoGenerate = false)
    val pageId: Int? = null,
    val id: String,
    val width: Int,
    val height: Int,
    val blurHash: String,
    val urlFull: String,
    val urlRegular: String,
    val userId: String,
    val name: String,
    val profileImageLarge: String
)