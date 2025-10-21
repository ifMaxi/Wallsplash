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
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardDoubleArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.feature.detail.presentation.state.PhotoDetailState
import com.maxidev.wallsplash.feature.favorite.presentation.model.FavoritesUi

// TODO: Set wallpaper feature
// TODO: Download feature

@Composable
fun PhotoDetailScreen(
    viewModel: PhotoDetailViewModel = hiltViewModel()
) {
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

@Composable
private fun ScreenContent(
    uiState: PhotoDetailState,
    uiEvents: (PhotoDetailUiEvents) -> Unit
) {
    val context = LocalContext.current
    val details = uiState.details
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSheet by remember { mutableStateOf(false) }

    // TODO: Fix window insets
    // TODO: Reduce recomposition in image.
    // TODO: Show status bar in white colors.

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
        ) {
            Box {
                ImageZoomItem(image = details?.imageRegular.orEmpty())

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
                        profileImage = details?.userProfileImage.orEmpty(),
                        name = details?.name.orEmpty(),
                        userName = details?.username.orEmpty(),
                        userLink = details?.userLink.orEmpty(),
                        dimensions = details?.dimensions.orEmpty(),
                        altDescription = details?.altDescription.orEmpty(),
                        likes = details?.likes.orEmpty(),
                        views = details?.views.orEmpty(),
                        downloads = details?.downloads.orEmpty(),
                        exifModel = details?.exifModel.orEmpty(),
                        exifExposureTime = details?.exifExposureTime.orEmpty(),
                        exifAperture = details?.exifAperture.orEmpty(),
                        exifFocalLength = details?.exifFocalLength.orEmpty(),
                        exifIso = details?.exifIso.orEmpty(),
                        tags = details?.tags.orEmpty(),
                        locationName = details?.locationName.orEmpty(),
                        sheetState = sheetState,
                        onDismissRequest = { showSheet = false },
                        onFavorite = {
                            uiEvents(
                                PhotoDetailUiEvents.OnSave(
                                    save = FavoritesUi()
                                        .copy(
                                            photo = details?.imageRegular.orEmpty(),
                                            width = details?.width ?: 0,
                                            height = details?.height ?: 0,
                                            blurHash = details?.blurHash.orEmpty()
                                        )
                                )
                            )
                            Toast.makeText(context, "Saved to favorites.", Toast.LENGTH_SHORT).show()
                        },
                        onShare = { uiEvents(PhotoDetailUiEvents.OnShare) },
                        onDownload = {
                            uiEvents(PhotoDetailUiEvents.OnDownload)
                            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show()
                        },
                        onSetAsWallpaper = { /* TODO: Set as wallp. logic */ }
                    )
                }
            }
        }
    }
}

/* Screen composables. */

@Composable
private fun ImageZoomItem(image: String) {
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
            model = image,
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

@[Composable Preview]
private fun ImageHeaderItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        ImageZoomItem(image = "")
    }
}

@Composable
private fun DetailInfoItem(
    profileImage: String,
    name: String,
    userName: String,
    userLink: String,
    dimensions: String,
    altDescription: String,
    likes: String,
    views: String,
    downloads: String,
    exifModel: String,
    exifExposureTime: String,
    exifAperture: String,
    exifFocalLength: String,
    exifIso: String,
    locationName: String,
    tags: List<String>,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onFavorite: () -> Unit,
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onSetAsWallpaper: () -> Unit
) {
    val modPadding = 8.dp
    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

    /* Intent variables */
    val context = LocalContext.current
    val browserIntent = Intent.ACTION_VIEW
    val intent = Intent(browserIntent, userLink.toUri())

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = true
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(modPadding)
                .verticalScroll(scrollState)
        ) {
            /* User info */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(modPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(modPadding)
            ) {
                AsyncImage(
                    model = profileImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = CircleShape
                        )
                )
                Column(modifier = Modifier.padding(modPadding)) {
                    Text(
                        text = name,
                        fontSize = 18.sp
                    )
                    Text(
                        text = userName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.padding(16.dp)) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options."
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = { context.startActivity(intent) },
                            text = { Text(text = "User profile") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.PersonPin,
                                    contentDescription = "User profile."
                                )
                            }
                        )
                        DropdownMenuItem(
                            onClick = onDownload,
                            text = { Text(text = "Download") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download image."
                                )
                            }
                        )
                        DropdownMenuItem(
                            onClick = onShare,
                            text = { Text(text = "Share") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share image."
                                )
                            }
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Utility buttons. */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(modPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(modPadding)
            ) {
                Button(
                    onClick = onFavorite,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SaveAlt,
                        contentDescription = "Save to favorites."
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = "Save to favorites")
                }
                Button(
                    onClick = onSetAsWallpaper,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Wallpaper,
                        contentDescription = "Set as wallpaper."
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(text = "Set as wallpaper")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Description */

            Text(
                text = altDescription,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(modPadding)
            )

            /* Location */

            // TODO: Open map with lat and long.

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(modPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(modPadding)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null
                )
                Text(text = locationName)
            }

            /* Likes, download and views info. */

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(modPadding),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = likes,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = views,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = downloads,
                    textAlign = TextAlign.Center
                )
            }

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Camera information. */

            FlowColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(modPadding),
                maxItemsInEachColumn = 3,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                itemHorizontalAlignment = Alignment.CenterHorizontally
            ) {
                val itemModifier = Modifier
                    .weight(1f)

                Text(
                    text = exifModel,
                    textAlign = TextAlign.Center,
                    modifier = itemModifier
                )
                Text(
                    text = exifFocalLength,
                    textAlign = TextAlign.Center,
                    modifier = itemModifier
                )
                Text(
                    text = exifExposureTime,
                    textAlign = TextAlign.Center,
                    modifier = itemModifier
                )
                Text(
                    text = exifAperture,
                    textAlign = TextAlign.Center,
                    modifier = itemModifier
                )
                Text(
                    text = exifIso,
                    textAlign = TextAlign.Center,
                    modifier = itemModifier
                )
                Text(
                    text = dimensions,
                    textAlign = TextAlign.Center,
                    modifier = itemModifier
                )
            }

            HorizontalDivider(modifier = Modifier.padding(modPadding))

            /* Tags */

            // TODO: Make borders
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(modPadding),
                horizontalArrangement = Arrangement.spacedBy(modPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items = tags) { index ->
                    Text(text = index)
                }
            }
        }
    }
}

@[Composable Preview]
private fun UserInfoItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }
    val showSheet by remember { mutableStateOf(true) }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        if (showSheet) {
            DetailInfoItem(
                profileImage = "",
                name = "Lorem Impsum",
                userName = "loremImpsum",
                userLink = "link",
                dimensions = "Dimensions\n2000 x 1000",
                altDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                likes = "Likes\n20.4k",
                views = "Views\n120.2k",
                downloads = "Downloads\n5.6k",
                exifModel = "Camera\nCanon EOS 40D",
                exifExposureTime = "Shutter Speed\n0.01s",
                exifAperture = "Aperture\nf/4.97",
                exifFocalLength = "Focal Length\n37mm",
                exifIso = "ISO\n130",
                tags = listOf("tag1", "tag2", "tag3", "tag4", "tag5", "tag6", "tag7", "tag8"),
                locationName = "Loremimptum, impsum",
                onDismissRequest = {},
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                onFavorite = {},
                onShare = {},
                onDownload = {},
                onSetAsWallpaper = {}
            )
        }
    }
}