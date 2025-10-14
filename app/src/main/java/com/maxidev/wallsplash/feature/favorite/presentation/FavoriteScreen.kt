@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.feature.favorite.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi
import java.util.UUID

@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.favorites.collectAsStateWithLifecycle()

    ScreenContent(
        allFavorites = state.favorites,
        uiEvents = { events ->
            when (events) {
                is FavoritesUiEvents.DeletePhoto -> {}
                is FavoritesUiEvents.DeleteMorePhotos -> {
                    viewModel.deleteSelectedPhotos(events.removeMore)
                }
            }
        }
    )
}

@Composable
private fun ScreenContent(
    allFavorites: List<FavoritesUi>,
    uiEvents: (FavoritesUiEvents) -> Unit
) {
    var activePhotoId by rememberSaveable { mutableStateOf<UUID?>(null) }
    var selectedIds by rememberSaveable { mutableStateOf(setOf<UUID>()) }
    val inSelectionMode = selectedIds.isNotEmpty()

    Scaffold(
        floatingActionButton = {
            if (inSelectionMode) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    ElevatedButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = { selectedIds = emptySet() },
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.Default.Close, "Cancel selection.")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "Selected photos: ${selectedIds.count()}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    IconButton(
                        modifier = Modifier.padding(8.dp),
                        onClick = {
                            uiEvents(FavoritesUiEvents.DeleteMorePhotos(selectedIds.toList()))
                            selectedIds = emptySet()
                        }
                    ) {
                        Icon(Icons.Default.DeleteSweep, "Delete selected photos.")
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            columns = StaggeredGridCells.Adaptive(120.dp),
            state = rememberLazyStaggeredGridState(),
            contentPadding = innerPadding
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = "My favorites",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            items(allFavorites, { key -> key.photoId }) { index ->
                val selected = selectedIds.contains(index.photoId)

                PhotoItem(
                    image = index.photo,
                    width = index.width.toFloat(),
                    height = index.height.toFloat(),
                    inSelectionMode = inSelectionMode,
                    selected = selected,
                    modifier = Modifier
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                if (inSelectionMode) {
                                    if (selected) {
                                        selectedIds -= index.photoId
                                    } else {
                                        selectedIds += index.photoId
                                    }
                                } else {
                                    activePhotoId = index.photoId
                                }
                            },
                            onLongClick = { selectedIds += index.photoId }
                        )
                )
            }
        }

        if (activePhotoId != null) {
            FullScreenImageItem(
                image = allFavorites.first { it.photoId == activePhotoId },
                onDismiss = { activePhotoId = null }
            )
        }
    }
}

@Composable
private fun PhotoItem(
    modifier: Modifier = Modifier,
    image: String,
    width: Float,
    height: Float,
    inSelectionMode: Boolean,
    selected: Boolean
) {
    Box(
        modifier = modifier
            .aspectRatio(ratio = (width / height))
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (selected) Modifier
                        .padding(8.dp)
                    else Modifier
                )
        )
        if (inSelectionMode) {
            val icon = if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked
            val tint =
                if (selected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f)

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun FullScreenImageItem(
    image: FavoritesUi,
    onDismiss: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Transformation states for panning, zooming and rotation.
        var scale by remember { mutableFloatStateOf(1f) }
        var rotation by remember { mutableFloatStateOf(0f) }
        var offSet by remember { mutableStateOf(Offset.Zero) }
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 2f)
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

        Scrim(onClose = onDismiss, modifier = Modifier.fillMaxSize())

        AsyncImage(
            model = image.photo,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offSet.x,
                    translationY = offSet.y
                )
                .transformable(state)
        )
    }
}

@Composable
private fun Scrim(
    modifier: Modifier = Modifier,
    onClose: () -> Unit
) {
    Box(
        modifier = modifier
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics(mergeDescendants = true) {
                contentDescription = "Close"
                onClick {
                    onClose()
                    true
                }
            }
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose()
                    true
                } else {
                    false
                }
            }
            .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}