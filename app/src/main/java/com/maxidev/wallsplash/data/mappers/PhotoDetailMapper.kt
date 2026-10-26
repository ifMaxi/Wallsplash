package com.maxidev.wallsplash.data.mappers

import com.maxidev.wallsplash.data.remote.dto.PhotoDetailDto
import com.maxidev.wallsplash.domain.model.PhotoDetail

fun PhotoDetailDto.asDomain() =
    this.let { data ->
        PhotoDetail(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            blurHash = data.blurHash.orEmpty(),
            altDescription = data.altDescription.orEmpty(),
            imageFull = data.urls?.full.orEmpty(),
            username = data.user?.username.orEmpty(),
            name = data.user?.name.orEmpty(),
            userProfileImage = data.user?.profileImage?.large.orEmpty(),
            exifModel = data.exif?.model.orEmpty(),
            exifExposureTime = data.exif?.exposureTime.orEmpty(),
            exifAperture = data.exif?.aperture.orEmpty(),
            exifFocalLength = data.exif?.focalLength.orEmpty(),
            exifIso = "${data.exif?.iso}",
            tags = data.tags?.mapNotNull { it?.title } ?: emptyList(),
            createdAt = data.createdAt.orEmpty(),
            updatedAt = data.updatedAt.orEmpty(),
            aspectRatio = "$width x $height",
            color = data.color.orEmpty(),
            userId = data.user?.id.orEmpty(),
            exifMake = data.exif?.make.orEmpty(),
            exifName = data.exif?.name.orEmpty(),
            locationName = data.location?.name.orEmpty(),
            city = data.location?.city.orEmpty(),
            country = data.location?.country.orEmpty()
        )
    }