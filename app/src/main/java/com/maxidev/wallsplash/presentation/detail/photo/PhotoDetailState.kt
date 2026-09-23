package com.maxidev.wallsplash.presentation.detail.photo

import com.maxidev.wallsplash.domain.model.PhotoDetail
import com.maxidev.wallsplash.utils.NetworkResourceUtil

data class PhotoDetailState(
    val details: NetworkResourceUtil<PhotoDetail> = NetworkResourceUtil.Loading()
)