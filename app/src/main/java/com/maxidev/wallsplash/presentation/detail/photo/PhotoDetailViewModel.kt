package com.maxidev.wallsplash.presentation.detail.photo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.data.repository.DetailRepositoryImpl
import com.maxidev.wallsplash.domain.model.PhotoDetail
import com.maxidev.wallsplash.utils.NetworkResourceUtil
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
    private val repository: DetailRepositoryImpl
) : ViewModel() {

    private val photoId = checkNotNull(savedStateHandle.get<String>("id"))

    private val _uiState: MutableStateFlow<NetworkResourceUtil<PhotoDetail>> =
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
                    NetworkResourceUtil.Success(repository.fetchPhotoDetails(photoId))
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

//    fun saveToDataBase(photo: Favorites) =
//        viewModelScope.launch {
//            insertPhotoUseCase(photo.asDomain())
//        }
}