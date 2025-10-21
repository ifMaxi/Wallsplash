package com.maxidev.wallsplash.common.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.wajahatiqbal.blurhash.BlurHashPainter

@Composable
fun <T: Any> CustomAsyncImage(
    modifier: Modifier = Modifier,
    model: T,
    blurHash: BlurHashPainter,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = blurHash,
        error = blurHash,
        modifier = modifier
    )
}