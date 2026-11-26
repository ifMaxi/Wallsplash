package com.maxidev.wallsplash.presentation.detail.users

import androidx.paging.PagingData
import com.maxidev.wallsplash.domain.model.User
import com.maxidev.wallsplash.domain.model.UserCollections
import com.maxidev.wallsplash.domain.model.UserPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class UserUiState(
    val user: User? = null,
    val userPhotos: Flow<PagingData<UserPhoto>> = emptyFlow(),
    val userCollections: Flow<PagingData<UserCollections>> = emptyFlow(),
    val isLoading: Boolean = false,
    val isError: String? = null
)