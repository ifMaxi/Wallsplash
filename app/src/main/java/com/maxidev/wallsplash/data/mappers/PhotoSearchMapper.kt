package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.remote.dto.SearchCollectionsDto
import com.maxidev.wallsplash.data.remote.dto.SearchPhotosDto
import com.maxidev.wallsplash.domain.model.SearchCollections
import com.maxidev.wallsplash.domain.model.SearchPhoto

fun SearchPhotosDto.asDomain() =
    this.results?.map { data ->
        SearchPhoto(
            id = data?.id.orEmpty(),
            width = data?.width ?: 0,
            height = data?.height ?: 0,
            blurHash = data?.blurHash.orEmpty(),
            urlRegular = data?.urls?.regular.orEmpty()
        )
    }

fun SearchCollectionsDto.asDomain() =
    this.results?.map { data ->
        SearchCollections(
            id = data?.id.orEmpty(),
            title = data?.title.orEmpty(),
            width = data?.coverPhoto?.width ?: 0,
            height = data?.coverPhoto?.height ?: 0,
            blurHash = data?.coverPhoto?.blurHash.orEmpty(),
            totalPhotos = data?.totalPhotos ?: 0,
            coverPhoto = data?.coverPhoto?.urls?.regular.orEmpty()
        )
    }