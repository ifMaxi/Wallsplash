package com.maxidev.wallsplash.presentation.detail.collection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.wallsplash.domain.repository.DetailRepository
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
class CollectionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: DetailRepository
) : ViewModel() {

    private val collectionId = checkNotNull(savedStateHandle.get<String>("id"))

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { stateData ->
                stateData.copy(
                    collectionData = try {
                        NetworkResourceUtil.Success(repository.fetchCollectionData(collectionId))
                    } catch (e: HttpException) {
                        NetworkResourceUtil.Error(e.message.toString())
                    } catch (e: IOException) {
                        NetworkResourceUtil.Error(e.message.toString())
                    }
                )
            }
        }

        _uiState.update { statePhotos ->
            statePhotos.copy(
                collectionPhoto = repository.fetchCollectionPhotos(collectionId)
                    .cachedIn(viewModelScope)
            )
        }
    }
}