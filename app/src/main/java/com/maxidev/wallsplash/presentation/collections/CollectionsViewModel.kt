package com.maxidev.wallsplash.presentation.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.wallsplash.domain.repository.CollectionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val repository: CollectionsRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CollectionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { photoState ->
            photoState.copy(
                collections = repository.fetchCollections()
                    .cachedIn(viewModelScope)
            )
        }
    }
}