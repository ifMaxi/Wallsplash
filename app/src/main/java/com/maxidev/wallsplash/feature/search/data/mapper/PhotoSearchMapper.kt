package com.maxidev.wallsplash.feature.search.data.mapper

import com.maxidev.wallsplash.feature.search.data.model.remote.SearchPhotosDto
import com.maxidev.wallsplash.feature.search.domain.model.SearchPhoto

fun SearchPhotosDto.asDomain() =
    this.results?.map { data ->
        SearchPhoto(
            id = data?.id.orEmpty(),
            width = data?.width ?: 0,
            height = data?.height ?: 0,
            color = data?.color.orEmpty(),
            blurHash = data?.blurHash.orEmpty(),
            urlRaw = data?.urls?.raw.orEmpty(),
            urlFull = data?.urls?.full.orEmpty(),
            urlRegular = data?.urls?.regular.orEmpty(),
            urlSmall = data?.urls?.small.orEmpty(),
            urlThumb = data?.urls?.thumb.orEmpty()
        )
    }