package com.maxidev.wallsplash.presentation.detail.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.wallsplash.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UserViewModel.Factory::class)
class UserViewModel @AssistedInject constructor(
    private val repository: UserRepository,
    @Assisted private val navKey: String
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navKey: String): UserViewModel
    }

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    user = repository.fetchUser(navKey)
                )
            }
        }
    }
}