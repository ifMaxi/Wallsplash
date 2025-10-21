@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.feature.favorite.presentation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi
import com.maxidev.wallsplash.feature.navigation.Destinations
import com.wajahatiqbal.blurhash.BlurHashPainter
import java.util.UUID

/* Extension that encapsulates the navigation code. */
fun NavGraphBuilder.favoriteDestination() {
    composable<Destinations.FavouritesView> {
        val viewModel = hiltViewModel<FavoritesViewModel>()
        val state by viewModel.favorites.collectAsStateWithLifecycle()

        ScreenContent(
            allFavorites = state.favorites,
            removePhotos = { viewModel.deleteSelectedPhotos(it) }
        )
    }
}

@Composable
private fun ScreenContent(
    allFavorites: List<FavoritesUi>,
    removePhotos: (List<UUID>) -> Unit
) {
    var activePhotoId by rememberSaveable { mutableStateOf<UUID?>(null) }
    var selectedIds by rememberSaveable { mutableStateOf(setOf<UUID>()) }
    val inSelectionMode = selectedIds.isNotEmpty()

    val roundedCornerShape = RoundedCornerShape(10.dp)

    Scaffold(
        floatingActionButton = {
            if (inSelectionMode) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledIconButton(
                        modifier = Modifier.padding(8.dp),
                        shape = roundedCornerShape,
                        onClick = {
                            removePhotos(selectedIds.toList())
                            selectedIds = emptySet()
                        }
                    ) {
                        Icon(Icons.Default.DeleteSweep, "Delete selected photos.")
                    }

                    ElevatedButton(
                        onClick = { selectedIds = emptySet() },
                        shape = roundedCornerShape
                    ) {
                        Icon(Icons.Default.Close, "Cancel selection.")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "${selectedIds.count()}"
                        )
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
            contentPadding = innerPadding,
            verticalItemSpacing = 2.dp,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
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
                    model = index,
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
                model = allFavorites.first { it.photoId == activePhotoId },
                onDismiss = { activePhotoId = null }
            )
        }
    }
}

@Composable
private fun PhotoItem(
    modifier: Modifier = Modifier,
    model: FavoritesUi,
    inSelectionMode: Boolean,
    selected: Boolean
) {
    val blurHolder = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    /**
     * Create an animation that expands and contracts when you select one or more images.
     */
    val imageScale by animateFloatAsState(
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        targetValue = if (selected) 0.9f else 1f,
        label = "scale"
    )

    Box(
        modifier = modifier
            .aspectRatio(ratio = (model.width.toFloat() / model.height.toFloat()))
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f))
            .graphicsLayer {
                // Implement the animation
                scaleX = imageScale
                scaleY = imageScale
            }
    ) {
        AsyncImage(
            model = model.photo,
            contentDescription = null,
            placeholder = blurHolder,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (selected) Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(10.dp))
                    else Modifier
                )
        )
        if (inSelectionMode) {
            val icon =
                if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked
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
    model: FavoritesUi,
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
            model = model.photo,
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