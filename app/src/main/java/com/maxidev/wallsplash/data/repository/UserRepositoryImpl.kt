package com.maxidev.wallsplash.data.repository

import com.maxidev.wallsplash.data.mappers.asDomain
import com.maxidev.wallsplash.data.remote.WallsplashApiService
import com.maxidev.wallsplash.domain.model.User
import com.maxidev.wallsplash.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: WallsplashApiService
) : UserRepository {

    override suspend fun fetchUser(id: String): User {
        return withContext(Dispatchers.IO) {
            apiService.getUserId(id)
                .asDomain()
        }
    }
}