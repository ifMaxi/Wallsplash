package com.maxidev.wallsplash.domain.repository

import com.maxidev.wallsplash.domain.model.User

interface UserRepository {

    suspend fun fetchUser(id: String): User
}