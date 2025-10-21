package com.maxidev.wallsplash.feature.search.data.mapper

import com.maxidev.wallsplash.feature.search.data.model.remote.SearchCollectionsDto
import com.maxidev.wallsplash.feature.search.data.model.remote.SearchPhotosDto
import com.maxidev.wallsplash.feature.search.domain.model.SearchCollections
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto

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