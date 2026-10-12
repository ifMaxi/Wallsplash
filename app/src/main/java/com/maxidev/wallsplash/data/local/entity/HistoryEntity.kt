package com.maxidev.wallsplash.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history_table",
    indices = [Index(value = ["query"], unique = true)]
)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val query: String,
    val timeStamp: Long
)