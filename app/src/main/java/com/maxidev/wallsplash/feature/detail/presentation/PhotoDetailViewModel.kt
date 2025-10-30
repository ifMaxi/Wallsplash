package com.maxidev.wallsplash.feature.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.common.utils.NetworkResourceUtil
import com.maxidev.wallsplash.feature.detail.domain.usecase.GetPhotoDetailUseCase
import com.maxidev.wallsplash.feature.detail.presentation.mapper.asUi
import com.maxidev.wallsplash.feature.detail.presentation.model.PhotoDetailUi
import com.maxidev.wallsplash.feature.favorite.domain.usecase.SavePhotoUseCase
import com.maxidev.wallsplash.feature.favorite.presentation.mapper.asDomain
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: GetPhotoDetailUseCase,
    private val insertPhotoUseCase: SavePhotoUseCase
) : ViewModel() {

    private val photoId = checkNotNull(savedStateHandle.get<String>("id"))

    private val _uiState: MutableStateFlow<NetworkResourceUtil<PhotoDetailUi>> =
        MutableStateFlow(NetworkResourceUtil.Loading())
    val uiState = _uiState.asStateFlow()

    init {
        resourceLoading(photoId)
    }

    private fun resourceLoading(photoId: String) {
        viewModelScope.launch {
            _uiState.value = NetworkResourceUtil.Loading()
            _uiState.update { state ->
                try {
                    NetworkResourceUtil.Success(useCase(photoId).asUi())
                } catch (e: IOException) {
                    NetworkResourceUtil.Error(message = e.message.toString())
                } catch (e: HttpException) {
                    NetworkResourceUtil.Error(message = e.message.toString())
                }
            }
        }
    }

    fun retryIfNotLoaded() {
        viewModelScope.launch {
            resourceLoading(photoId = photoId)
        }
    }

    fun saveToDataBase(photo: FavoritesUi) =
        viewModelScope.launch {
            insertPhotoUseCase(photo.asDomain())
        }
}