package com.maxidev.wallsplash.feature.photos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maxidev.wallsplash.feature.photos.domain.model.Collections
import com.maxidev.wallsplash.feature.photos.domain.model.Photos
import com.maxidev.wallsplash.feature.photos.domain.usecase.AllCollectionsUseCase
import com.maxidev.wallsplash.feature.photos.domain.usecase.AllPhotosUseCase
import com.maxidev.wallsplash.feature.photos.presentation.mapper.asUi
import com.maxidev.wallsplash.feature.photos.presentation.mapper.toUi
import com.maxidev.wallsplash.feature.photos.presentation.state.PhotosUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val photosUseCase: AllPhotosUseCase,
    private val collectionsUseCase: AllCollectionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhotosUiState())
    val uiState = _uiState.asStateFlow()

    init {
        allPhotos()
        allCollections()
    }

    // Displays a list of photos.
    private fun allPhotos() {
        _uiState.update { photoState ->
            photoState.copy(
                photos = photosUseCase()
                    .map { model -> model.map(Photos::toUi) }
                    .cachedIn(viewModelScope)
            )
        }
    }

    // Displays a list of collections.
    private fun allCollections() {
        _uiState.update { collectionState ->
            collectionState.copy(
                collections = collectionsUseCase()
                    .map { model -> model.map(Collections::asUi) }
                    .cachedIn(viewModelScope)
            )
        }
    }

    /**
     * Callback that will cause a screen update to display new updated data.
     */
    fun refreshAll() {
        _uiState.update { refresh ->
            refresh.copy(isRefreshing = true)
        }

        viewModelScope.launch {
            delay(1300)

            allPhotos()
            allCollections()

            _uiState.update { stopRefresh ->
                stopRefresh.copy(isRefreshing = false)
            }
        }
    }
}