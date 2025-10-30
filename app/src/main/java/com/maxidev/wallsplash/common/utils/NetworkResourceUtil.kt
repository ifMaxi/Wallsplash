package com.maxidev.wallsplash.common.utils

sealed class NetworkResourceUtil<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResourceUtil<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResourceUtil<T>(data, message)
    class Loading<T> : NetworkResourceUtil<T>()
}