package com.maxidev.wallsplash.presentation.detail.users

import com.maxidev.wallsplash.domain.model.User

data class UserUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isError: String? = null
)