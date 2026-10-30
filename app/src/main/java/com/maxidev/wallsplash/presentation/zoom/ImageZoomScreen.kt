package com.maxidev.wallsplash.presentation.zoom

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import coil3.compose.AsyncImage

@Composable
fun ImageZoomScreen(photo: String) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var rotation by remember { mutableFloatStateOf(0f) }
        var offSet by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)
            rotation += rotationChange

            val extraWidth = (scale - 1) * constraints.maxWidth
            val extraHeight = (scale - 1) * constraints.maxHeight
            val maxX = extraWidth / 2
            val maxY = extraHeight / 2

            offSet = Offset(
                x = (offSet.x + scale * offsetChange.x).coerceIn(-maxX, maxX),
                y = (offSet.y + scale * offsetChange.y).coerceIn(-maxY, maxY)
            )
        }

        AsyncImage(
            model = photo,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = -offSet.x * scale,
                    translationY = -offSet.y * scale
                )
                .transformable(state)
        )
    }
}