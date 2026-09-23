@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maxidev.wallsplash.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.wallsplash.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
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
                searchPhotos = repository.fetchSearchPhotos(input)
                    .cachedIn(viewModelScope),
                searchCollections = repository.fetchSearchCollections(input)
                    .cachedIn(viewModelScope)
            )
        }
    }
}