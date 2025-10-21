package com.maxidev.wallsplash.feature.favorite.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.feature.favorite.domain.model.Favorites
import com.maxidev.wallsplash.feature.favorite.domain.usecase.DeleteSelectedPhotosUseCase
import com.maxidev.wallsplash.feature.favorite.domain.usecase.GetFavoritesUseCase
import com.maxidev.wallsplash.feature.favorite.presentation.mapper.asUi
import com.maxidev.wallsplash.feature.favorite.presentation.state.FavoriteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteSelectedPhotosUseCase: DeleteSelectedPhotosUseCase
) : ViewModel() {

    val favorites: StateFlow<FavoriteUiState> =
        getFavoritesUseCase()
            .map {
                val mapper = it.map(Favorites::asUi)

                FavoriteUiState(mapper)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = FavoriteUiState()
            )

    fun deleteSelectedPhotos(photos: List<UUID>) =
        viewModelScope.launch {
            deleteSelectedPhotosUseCase(photos)
        }
}