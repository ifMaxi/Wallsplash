@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maxidev.wallsplash.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.maxidev.wallsplash.feature.search.domain.model.SearchCollections
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto
import com.maxidev.wallsplash.feature.search.domain.usecase.SearchCollectionUseCase
import com.maxidev.wallsplash.feature.search.domain.usecase.SearchPhotoUseCase
import com.maxidev.wallsplash.feature.search.presentation.mapper.asUi
import com.maxidev.wallsplash.feature.search.presentation.state.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPhotoUseCase: SearchPhotoUseCase,
    private val searchCollectionsUseCase: SearchCollectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _input = MutableStateFlow("")
    val input = _input.asStateFlow()

    fun onInputChange(newValue: String) {
        _input.value = newValue
    }

    /**
     * Callback that performs a search using the given criteria.
     * It makes two calls together to find the desired elements.
     *
     * @param input string containing the search terms.
     */
    fun onSearch(input: String) {
        _uiState.update { state ->
            state.copy(
                searchPhotos = searchPhotoUseCase(input)
                    .map { result -> result.map(SearchPhoto::asUi) }
                    .cachedIn(viewModelScope),
                searchCollections = searchCollectionsUseCase(input)
                    .map { result -> result.map(SearchCollections::asUi) }
                    .cachedIn(viewModelScope)
            )
        }
    }
}