package com.maxidev.wallsplash.feature.detail.presentation.mapper

import com.maxidev.wallsplash.feature.detail.domain.model.PhotoDetail
import com.maxidev.wallsplash.feature.detail.presentation.model.PhotoDetailUi

fun PhotoDetail.asUi() =
    PhotoDetailUi(
        id = id,
        width = width,
        height = height,
        blurHash = blurHash,
        dimensions = "$width x $height".ifEmpty { "Unknown" },
        altDescription = altDescription,
        likes = "Likes\n$likes",
        views = "Views\n$views",
        downloads = "Downloads\n$downloads",
        imageFull = imageFull,
        imageRegular = imageRegular,
        username = username,
        name = name,
        userProfileImage = userProfileImage,
        userLink = userLink,
        exifModel = exifModel.ifEmpty { "Unknown" },
        exifExposureTime = if (exifExposureTime.isEmpty()) "Unknown" else "${exifExposureTime}s",
        exifAperture = if (exifAperture.isEmpty()) "Unknown" else "f/${exifAperture}",
        exifFocalLength = if (exifExposureTime.isEmpty()) "Unknown" else "${exifFocalLength}mm",
        exifIso = "${if (exifIso == 0) "Unknown" else exifIso}",
        tags = tags
    )