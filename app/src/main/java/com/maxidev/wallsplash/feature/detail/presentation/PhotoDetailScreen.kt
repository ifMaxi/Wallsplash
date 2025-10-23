@file:OptIn(ExperimentalCoilApi::class, ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.feature.detail.presentation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Iso
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.feature.detail.presentation.model.PhotoDetailUi
import com.maxidev.wallsplash.feature.detail.presentation.state.PhotoDetailState
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi
import com.maxidev.wallsplash.feature.navigation.Destinations

// TODO: Set wallpaper feature
// TODO: Download feature

/* Extension that encapsulates the navigation code. */
fun NavGraphBuilder.photoDetailDestination() {
    composable<Destinations.PhotoDetailsView> {
        val viewModel = hiltViewModel<PhotoDetailViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        ScreenContent(
            uiState = state,
            uiEvents = { events ->
                when (events) {
                    PhotoDetailUiEvents.OnShare -> {}
                    PhotoDetailUiEvents.OnDownload -> {}
                    is PhotoDetailUiEvents.OnSave -> { viewModel.saveToDataBase(events.save) }
                }
            }
        )
    }
}

@Composable
private fun ScreenContent(
    uiState: PhotoDetailState,
    uiEvents: (PhotoDetailUiEvents) -> Unit
) {
    val context = LocalContext.current
    val details = uiState.details
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSheet by remember { mutableStateOf(false) }

    /* Intent variables */
    val browserIntent = Intent.ACTION_VIEW
    val intent = Intent(browserIntent, details?.userLink?.toUri())

    // TODO: Fix window insets

    Scaffold { innerPadding ->
        if (details != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
            ) {
                Box {
                    ImageZoomItem(model = details)

                    IconButton(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        onClick = { showSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardDoubleArrowUp,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    if (showSheet) {
                        DetailInfoItem(
                            model = details,
                            sheetState = sheetState,
                            onDismissRequest = { showSheet = false },
                            onSaveToFavorite = {
                                uiEvents(
                                    PhotoDetailUiEvents.OnSave(
                                        save = FavoritesUi()
                                            .copy(
                                                photo = details.imageRegular,
                                                width = details.width,
                                                height = details.height,
                                                blurHash = details.blurHash
                                            )
                                    )
                                )
                                Toast.makeText(context, "Saved to favorites.", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            onShare = { uiEvents(PhotoDetailUiEvents.OnShare) },
                            onDownload = {
                                uiEvents(PhotoDetailUiEvents.OnDownload)
                                Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
                            },
                            onSetAsWallpaper = { /* TODO: Set as wallp. logic */ },
                            onUserProfile = { context.startActivity(intent) }
                        )
                    }
                }
            }
        }
    }
}

/* Screen composables. */

@Composable
private fun ImageZoomItem(model: PhotoDetailUi) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Transformation states for panning, zooming and rotation.
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
            model = model.imageFull,
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
private fun DetailInfoItem(
    model: PhotoDetailUi,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onSaveToFavorite: () -> Unit,
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onSetAsWallpaper: () -> Unit,
    onUserProfile: () -> Unit,
) {
    val modPadding = 8.dp
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = true,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(modPadding)
                .verticalScroll(scrollState)
        ) {
            /* User info */
            UserInfoItem(
                model = model,
                onShare = onShare,
                onDownload = onDownload,
                onSetAsWallpaper = onSetAsWallpaper,
                onUserProfile = onUserProfile,
                onSaveToFavorites = onSaveToFavorite
            )

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Description */
            DescriptionItem(model = model)

            /* Likes, download and views info. */
            StatisticsDataItem(model = model)

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Camera information. */
            CameraInformationItem(model = model)

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Tags */
            TagListItem(
                model = model,
                onClick = { /* TODO: Navigate to tag result */ }
            )
        }
    }
}

@Composable
private fun UserInfoItem(
    model: PhotoDetailUi,
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onSetAsWallpaper: () -> Unit,
    onUserProfile: () -> Unit,
    onSaveToFavorites: () -> Unit
) {
    val modPadding = 8.dp
    val placeholderColor = ColorPainter(Color.DarkGray)
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(modPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(modPadding)
    ) {
        AsyncImage(
            model = model.userProfileImage,
            contentDescription = model.name,
            contentScale = ContentScale.Crop,
            placeholder = placeholderColor,
            error = placeholderColor,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
        )
        Column(
            modifier = Modifier.padding(modPadding),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = model.name,
                fontSize = 18.sp
            )
            Text(
                text = model.username,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.padding(16.dp)) {
            IconButton(onClick = { expanded = !expanded }) {
                val icon = if (expanded) Icons.Default.MoreHoriz else Icons.Default.MoreVert
                val iconDescription = if (expanded) "Hide options." else "Show options."

                Icon(icon, iconDescription)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(10.dp)
            ) {
                DropdownMenuItem(
                    onClick = onUserProfile,
                    text = { Text(text = "User profile") },
                    leadingIcon = { Icon(Icons.Default.AccountBox, "User profile.") }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    onClick = onSaveToFavorites,
                    text = { Text(text = "Save to favorites") },
                    leadingIcon = { Icon(Icons.Default.Favorite, "Save to favorites.") }
                )
                DropdownMenuItem(
                    onClick = onDownload,
                    text = { Text(text = "Download") },
                    leadingIcon = { Icon(Icons.Default.Download, "Download image.") }
                )
                DropdownMenuItem(
                    onClick = onSetAsWallpaper,
                    text = { Text(text = "Set as wallpaper") },
                    leadingIcon = { Icon(Icons.Default.Wallpaper, "Set as wallpaper.") }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    onClick = onShare,
                    text = { Text(text = "Share") },
                    leadingIcon = { Icon(Icons.Default.Share, "Share image.") }
                )
            }
        }
    }
}

@Composable
private fun DescriptionItem(model: PhotoDetailUi) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\"${model.altDescription}\"",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Composable
private fun StatisticsDataItem(model: PhotoDetailUi) {
    val centerText = TextAlign.Center

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = model.views,
            textAlign = centerText
        )
        Text(
            text = model.downloads,
            textAlign = centerText
        )
        Text(
            text = model.likes,
            textAlign = centerText
        )
    }
}

@Composable
private fun CameraInformationItem(model: PhotoDetailUi) {
    val containerColor = Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ListItem(
            headlineContent = { Text(text = model.exifModel) },
            supportingContent = { Text(text = "Camera") },
            leadingContent = { Icon(Icons.Default.PhotoCamera, "Camera") },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
        ListItem(
            headlineContent = { Text(text = model.exifFocalLength) },
            supportingContent = { Text(text = "Focal length") },
            leadingContent = { Icon(Icons.Default.Visibility, "Focal length") },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
        ListItem(
            headlineContent = { Text(text = model.exifExposureTime) },
            supportingContent = { Text(text = "Shutter speed") },
            leadingContent = { Icon(Icons.Default.ShutterSpeed, "Shutter speed") },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
        ListItem(
            headlineContent = { Text(text = model.exifAperture) },
            supportingContent = { Text(text = "Aperture") },
            leadingContent = { Icon(Icons.Default.Camera, "Aperture") },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
        ListItem(
            headlineContent = { Text(text = model.exifIso) },
            supportingContent = { Text(text = "Iso") },
            leadingContent = { Icon(Icons.Default.Iso, "Camera iso") },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
        ListItem(
            headlineContent = { Text(text = model.dimensions) },
            supportingContent = { Text(text = "Dimensions") },
            leadingContent = { Icon(Icons.Default.PhotoSizeSelectActual, "Dimensions") },
            colors = ListItemDefaults.colors(containerColor = containerColor)
        )
    }
}

@Composable
private fun TagListItem(
    model: PhotoDetailUi,
    onClick: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = model.tags) { index ->
            SuggestionChip(
                onClick = onClick,
                label = {
                    Text(text = index)
                },
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

/* Preview composables. */

private val modelPreview = PhotoDetailUi(
    id = "",
    width = 5760,
    height = 3840,
    dimensions = "5760 x 3840",
    blurHash = "LFE2b~IV00xv_3ofx]t7?bV@V@j[",
    altDescription = "a brown and white cow standing in a field",
    likes = "Likes\n100",
    views = "Views\n5000",
    downloads = "Downloads\n200",
    imageFull = "",
    imageRegular = "",
    username = "johndoe",
    name = "John Doe",
    userProfileImage = "",
    userLink = "",
    exifModel = "Canon EOS R5",
    exifExposureTime = "1/125s",
    exifAperture = "f/8.0",
    exifFocalLength = "50mm",
    exifIso = "100",
    tags = listOf("nature", "animal", "cow", "field")
)

@Preview
@Composable
private fun ImageHeaderItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        ImageZoomItem(
            model = modelPreview
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserInfoItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }
    val showSheet by remember { mutableStateOf(true) }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        if (showSheet) {
            DetailInfoItem(
                model = modelPreview,
                onDismissRequest = {},
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onSaveToFavorite = {},
                onShare = {},
                onDownload = {},
                onSetAsWallpaper = {},
                onUserProfile = {}
            )
        }
    }
}