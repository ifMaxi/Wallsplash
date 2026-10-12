@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.maxidev.wallsplash.presentation.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.wallsplash.domain.model.History
import com.maxidev.wallsplash.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    val textFieldState = TextFieldState()

    val currentFilter = MutableStateFlow("")

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

    // History
    
    val historyState: StateFlow<List<String>> =
        currentFilter
            .debounce(150.milliseconds)
            .map { it.trim() }
            .distinctUntilChanged()
            .flatMapLatest { repository.getHistory(it) }
            .map { list -> list.map { it.query } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun clearAll() = viewModelScope.launch { repository.deleteAllHistory() }

    fun deleteItem(query: String) = viewModelScope.launch { repository.deleteHistory(query) }

    fun saveInDb(data: History) {
        viewModelScope.launch {
            repository.upsertHistory(data)
        }
    }
}