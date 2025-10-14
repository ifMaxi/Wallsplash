package com.maxidev.wallsplash.feature.detail.data.mapper

import com.maxidev.wallsplash.feature.detail.data.model.remote.PhotoDetailDto
import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail

fun PhotoDetailDto.asDomain() =
    this.let { data ->
        PhotoDetail(
            id = data.id.orEmpty(),
            width = data.width ?: 0,
            height = data.height ?: 0,
            color = data.color.orEmpty(),
            blurHash = data.blurHash.orEmpty(),
            altDescription = data.altDescription.orEmpty(),
            likes = data.likes ?: 0,
            views = data.views ?: 0,
            downloads = data.downloads ?: 0,
            imageRaw = data.urls?.raw.orEmpty(),
            imageFull = data.urls?.full.orEmpty(),
            imageRegular = data.urls?.regular.orEmpty(),
            imageSmall = data.urls?.small.orEmpty(),
            imageThumb = data.urls?.thumb.orEmpty(),
            userId = data.user?.id.orEmpty(),
            username = data.user?.username.orEmpty(),
            name = data.user?.name.orEmpty(),
            userProfileImage = data.user?.profileImage?.large.orEmpty(),
            userLink = data.user?.links?.html.orEmpty(),
            exifMake = data.exif?.make.orEmpty(),
            exifModel = data.exif?.model.orEmpty(),
            exifName = data.exif?.name.orEmpty(),
            exifExposureTime = data.exif?.exposureTime.orEmpty(),
            exifAperture = data.exif?.aperture.orEmpty(),
            exifFocalLength = data.exif?.focalLength.orEmpty(),
            exifIso = data.exif?.iso ?: 0,
            locationCity = data.location?.city.orEmpty(),
            locationCountry = data.location?.country.orEmpty(),
            locationPositionLatitude = data.location?.position?.latitude ?: 0.0,
            locationPositionLongitude = data.location?.position?.longitude ?: 0.0,
            tags = data.tags?.mapNotNull { it?.title } ?: emptyList()
        )
    }