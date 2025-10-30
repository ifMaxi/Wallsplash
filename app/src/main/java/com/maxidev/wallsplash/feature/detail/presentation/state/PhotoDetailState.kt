package com.maxidev.wallsplash.feature.detail.presentation.state

import com.maxidev.wallsplash.common.utils.NetworkResourceUtil
import com.maxidev.wallsplash.feature.detail.presentation.model.PhotoDetailUi

data class PhotoDetailState(
    val details: NetworkResourceUtil<PhotoDetailUi> = NetworkResourceUtil.Loading()
)