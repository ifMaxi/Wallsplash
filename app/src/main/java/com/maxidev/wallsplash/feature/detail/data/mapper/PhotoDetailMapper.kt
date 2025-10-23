package com.maxidev.wallsplash.feature.detail.data.mapper

import com.maxidev.wallsplash.feature.detail.data.model.remote.PhotoDetailDto
import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail

fun PhotoDetailDto.asDomain() =
    this.let { data ->
        PhotoDetail(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            blurHash = data.blurHash.orEmpty(),
            altDescription = data.altDescription.orEmpty(),
            likes = data.likes ?: 0,
            views = data.views ?: 0,
            downloads = data.downloads ?: 0,
            imageFull = data.urls?.full.orEmpty(),
            imageRegular = data.urls?.regular.orEmpty(),
            username = data.user?.username.orEmpty(),
            name = data.user?.name.orEmpty(),
            userProfileImage = data.user?.profileImage?.large.orEmpty(),
            userLink = data.user?.links?.html.orEmpty(),
            exifModel = data.exif?.model.orEmpty(),
            exifExposureTime = data.exif?.exposureTime.orEmpty(),
            exifAperture = data.exif?.aperture.orEmpty(),
            exifFocalLength = data.exif?.focalLength.orEmpty(),
            exifIso = data.exif?.iso ?: 0,
            tags = data.tags?.mapNotNull { it?.title } ?: emptyList()
        )
    }