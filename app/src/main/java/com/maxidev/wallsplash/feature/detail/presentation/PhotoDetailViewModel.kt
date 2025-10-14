package com.maxidev.wallsplash.feature.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.feature.detail.domain.usecase.GetPhotoDetailUseCase
import com.maxidev.wallsplash.feature.detail.presentation.mapper.asUi
import com.maxidev.wallsplash.feature.detail.presentation.state.PhotoDetailState
import com.maxidev.wallsplash.feature.favorite.domain.usecase.SavePhotoUseCase
import com.maxidev.wallsplash.feature.favorite.presentation.mapper.asDomain
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: GetPhotoDetailUseCase,
    private val insertPhotoUseCase: SavePhotoUseCase
) : ViewModel() {

    private val photoId = checkNotNull(savedStateHandle.get<String>("id"))

    private val _uiState = MutableStateFlow(PhotoDetailState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    details = useCase(photoId)
                        .asUi()
                )
            }
        }
    }

    fun saveToDataBase(photo: FavoritesUi) =
        viewModelScope.launch {
            insertPhotoUseCase(photo.asDomain())
        }

    // TODO: Loading state
}