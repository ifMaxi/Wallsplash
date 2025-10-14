package com.maxidev.wallsplash.feature.collections.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maxidev.wallsplash.feature.collections.domain.model.CollectionPhotos
import com.maxidev.wallsplash.feature.collections.domain.usecase.GetCollectionDataUseCase
import com.maxidev.wallsplash.feature.collections.domain.usecase.GetCollectionPhotosUseCase
import com.maxidev.wallsplash.feature.collections.presentation.mapper.asUi
import com.maxidev.wallsplash.feature.collections.presentation.state.CollectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val collectionDataUseCase: GetCollectionDataUseCase,
    private val collectionPhotosUseCase: GetCollectionPhotosUseCase
) : ViewModel() {

    private val collectionId = checkNotNull(savedStateHandle.get<String>("id"))

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { stateData ->
                stateData.copy(
                    collectionData = collectionDataUseCase(collectionId)
                        .asUi()
                )
            }
        }

        _uiState.update { statePhotos ->
            statePhotos.copy(
                collectionPhoto = collectionPhotosUseCase(collectionId)
                    .cachedIn(viewModelScope)
                    .map { pagingData -> pagingData.map(CollectionPhotos::asUi) }
            )
        }
    }
}