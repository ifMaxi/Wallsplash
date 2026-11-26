package com.maxidev.wallsplash.domain.repository

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.User
import com.maxidev.wallsplash.domain.model.UserCollections
import com.maxidev.wallsplash.domain.model.UserPhoto
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun fetchUser(id: String): User

    fun fetchUserPhotos(id: String): Flow<PagingData<UserPhoto>>

    fun fetchUserCollections(id: String): Flow<PagingData<UserCollections>>
}