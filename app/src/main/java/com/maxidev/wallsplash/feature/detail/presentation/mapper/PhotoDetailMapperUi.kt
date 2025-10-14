package com.maxidev.wallsplash.feature.detail.presentation.mapper

import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail
import com.maxidev.wallsplash.feature.detail.presentation.model.PhotoDetailUi

fun PhotoDetail.asUi() =
    PhotoDetailUi(
        id = id,
        width = width,
        height = height,
        dimensions = "Dimensions\n$width x $height",
        color = color,
        blurHash = blurHash,
        altDescription = altDescription,
        likes = "Likes\n$likes",
        views = "Views\n$views",
        downloads = "Downloads\n$downloads",
        imageRaw = imageRaw,
        imageFull = imageFull,
        imageRegular = imageRegular,
        imageSmall = imageSmall,
        imageThumb = imageThumb,
        userId = userId,
        username = username,
        name = name,
        userProfileImage = userProfileImage,
        userLink = userLink,
        exifMake = exifMake,
        exifModel = "Camera\n$exifModel",
        exifName = exifName,
        exifExposureTime = "Shutter Speed\n${exifExposureTime}s",
        exifAperture = "Aperture\nf/$exifAperture",
        exifFocalLength = "Focal Length\n${exifFocalLength}mm",
        exifIso = "ISO\n$exifIso",
        locationName = "$locationCity, $locationCountry",
        locationPositionLatitude = locationPositionLatitude.toString(),
        locationPositionLongitude = locationPositionLongitude.toString(),
        tags = tags
    )