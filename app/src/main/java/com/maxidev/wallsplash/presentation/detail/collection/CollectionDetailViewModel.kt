package com.maxidev.wallsplash.presentation.detail.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.wallsplash.domain.repository.DetailRepository
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

@HiltViewModel(assistedFactory = CollectionDetailViewModel.Factory::class)
class CollectionDetailViewModel @AssistedInject constructor(
    private val repository: DetailRepository,
    @Assisted val navKey: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: String): CollectionDetailViewModel
    }

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val dataDeferred = async { repository.fetchCollectionData(navKey) }
                val photosDeferred = async {
                    repository.fetchCollectionPhotos(navKey)
                        .cachedIn(viewModelScope)
                }

                _uiState.update { state ->
                    state.copy(
                        collectionData = dataDeferred.await(),
                        collectionPhoto = photosDeferred.await(),
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