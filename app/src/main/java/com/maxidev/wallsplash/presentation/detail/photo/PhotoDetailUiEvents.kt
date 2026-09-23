package com.maxidev.wallsplash.presentation.detail.photo

import com.maxidev.wallsplash.domain.model.Favorites

interface PhotoDetailUiEvents {
    data object OnShare : PhotoDetailUiEvents
    data object OnDownload : PhotoDetailUiEvents
    data class OnSave(val save: Favorites) : PhotoDetailUiEvents
}