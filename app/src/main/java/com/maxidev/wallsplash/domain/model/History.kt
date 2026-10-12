package com.maxidev.wallsplash.domain.model

data class History(
    val id: Long = 0,
    val query: String,
    val timeStamp: Long = System.currentTimeMillis()
)