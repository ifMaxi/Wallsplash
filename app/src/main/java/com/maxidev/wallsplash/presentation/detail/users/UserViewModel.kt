package com.maxidev.wallsplash.presentation.detail.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.wallsplash.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@HiltViewModel(assistedFactory = UserViewModel.Factory::class)
class UserViewModel @AssistedInject constructor(
    private val repository: UserRepository,
    @Assisted private val navKey: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: String): UserViewModel
    }

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val userDeferred = async { repository.fetchUser(navKey) }
                val userPhotosDeferred = async {
                    repository.fetchUserPhotos(id = navKey)
                        .cachedIn(viewModelScope)
                }
                val userCollectionsDeferred = async {
                    repository.fetchUserCollections(id = navKey)
                        .cachedIn(viewModelScope)
                }

                _uiState.update { state ->
                    state.copy(
                        user = userDeferred.await(),
                        userPhotos = userPhotosDeferred.await(),
                        userCollections = userCollectionsDeferred.await(),
                        isLoading = false
                    )
                }
            } catch (e: HttpException) {
                _uiState.update { state ->
                    state.copy(isLoading = false, isError = e.message())
                }
            } catch (e: IOException) {
                _uiState.update { state ->
                    state.copy(isLoading = false, isError = e.message)
                }
            }
        }
    }
}