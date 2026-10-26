@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.presentation.detail.photo

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.material3.ToggleFloatingActionButtonDefaults.animateIcon
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.broadcast.download.AndroidDownloader
import com.maxidev.wallsplash.domain.model.PhotoDetail
import com.maxidev.wallsplash.utils.NetworkResourceUtil
import com.maxidev.wallsplash.wallpaper.WallpaperHelper
import com.maxidev.wallsplash.wallpaper.WallpaperTarget
import com.wajahatiqbal.blurhash.BlurHashPainter
import kotlinx.coroutines.launch

@Composable
fun PhotoDetailScreen(
    viewModel: PhotoDetailViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenContent(
        uiState = state,
        onRetry = {}
    )
}

@Composable
private fun ScreenContent(
    uiState: NetworkResourceUtil<PhotoDetail>,
    onRetry: () -> Unit
) {
    val details = uiState.data

    DetailContent(model = details ?: return)
}

@Composable
private fun DetailContent(
    model: PhotoDetail
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyListState()
    val focusRequest = remember { FocusRequester() }
    var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val downloadImage = AndroidDownloader(context)
    val shareIntentChooser = Intent.createChooser(
        Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, model.imageFull)
        }, "Share image."
    )

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        floatingActionButton = {
            FloatingActionButtonMenu(
                expanded = fabMenuExpanded,
                button = {
                    val tooltipState = rememberTooltipState()
                    val positionProvider =
                        TooltipDefaults.rememberTooltipPositionProvider(
                            if (fabMenuExpanded) TooltipAnchorPosition.Start else TooltipAnchorPosition.Above
                        )

                    TooltipBox(
                        positionProvider = positionProvider,
                        tooltip = {
                            PlainTooltip(
                                modifier = Modifier
                                    .semantics {
                                        liveRegion = LiveRegionMode.Assertive
                                        paneTitle = "Toggle menu"
                                    }
                            ) { Text(text = "Toggle menu") }
                        },
                        state = tooltipState
                    ) {
                        ToggleFloatingActionButton(
                            modifier = Modifier
                                .semantics {
                                    traversalIndex = -1f
                                    stateDescription =
                                        if (fabMenuExpanded) "Expanded" else "Collapsed"
                                    contentDescription = "Toggle menu"
                                }
                                .focusRequester(focusRequest),
                            checked = fabMenuExpanded,
                            onCheckedChange = { fabMenuExpanded = !fabMenuExpanded }
                        ) {
                            val icon by remember {
                                derivedStateOf {
                                    if (checkedProgress > 0.5f) R.drawable.close else R.drawable.add
                                }
                            }

                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .animateIcon(checkedProgress = { checkedProgress })
                            )
                        }
                    }
                }
            ) {
                val items = listOf(
                    R.drawable.download to "Download",
                    R.drawable.wallpaper to "Set as wallpaper",
                    R.drawable.share to "Share"
                )

                items.forEachIndexed { index, pair ->
                    FloatingActionButtonMenuItem(
                        modifier = Modifier
                            .semantics {
                                isTraversalGroup = true
                                if (index == items.size - 1) {
                                    customActions =
                                        listOf(
                                            CustomAccessibilityAction(
                                                label = "Close menu",
                                                action = {
                                                    fabMenuExpanded = false
                                                    true
                                                }
                                            )
                                        )
                                }
                            }
                            .then(
                                if (index == 0) {
                                    Modifier.onKeyEvent {
                                        if (it.type == KeyEventType.KeyDown &&
                                            (it.key == Key.DirectionUp ||
                                                    it.key == Key.NumPadDirectionUp ||
                                                    (it.isShiftPressed && it.key == Key.Tab))
                                        ) {
                                            focusRequest.requestFocus()
                                            return@onKeyEvent true
                                        }
                                        return@onKeyEvent false
                                    }
                                } else Modifier
                            ),
                        onClick = {
                            fabMenuExpanded = false

                            when (index) {
                                0 -> { downloadImage.download(model.imageFull) }
                                1 -> { showDialog = true }
                                else -> {
                                    scope.launch { context.startActivity(shareIntentChooser) }
                                }
                            }
                        },
                        icon = { Icon(painterResource(pair.first), null) },
                        text = {
                            var textLayoutResult: TextLayoutResult? by remember {
                                mutableStateOf(null)
                            }

                            Text(
                                text = pair.second,
                                onTextLayout = { textLayoutResult = it }
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        BackHandler(fabMenuExpanded) { fabMenuExpanded = false }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
            state = lazyState
        ) {
            item {
                PhotoHeader(
                    image = model.imageFull,
                    contentDescription = model.altDescription,
                    width = model.width,
                    height = model.height,
                    hashPainter = model.blurHash,
                    onClick = { /* TODO: Open zoom image. */ }
                )
            }
            item {
                UserInfoItem(
                    profileImage = model.userProfileImage,
                    name = model.name,
                    userName = model.username,
                    color = model.color
                )
            }
            item {
                DescriptionItem(str = model.altDescription)
            }
            item {
                CameraInformationItem(
                    exifMake = model.exifMake,
                    exifName = model.exifName,
                    exifModel = model.exifModel,
                    exifFocalLength = model.exifFocalLength,
                    exifExposureTime = model.exifExposureTime,
                    exifAperture = model.exifAperture,
                    exifIso = model.exifIso,
                    aspectRatio = model.aspectRatio
                )
            }
            item {
                TagListItem(
                    tags = model.tags,
                    onClick = {}
                )
            }
        }

        if (showDialog) {
            WallpaperTargetDialog(
                onDismiss = { showDialog = false },
                onSelected = {
                    showDialog = false

                    scope.launch {
                        WallpaperHelper.applyWallpaper(
                            context = context,
                            imageUrl = model.imageFull,
                            target = WallpaperTarget.HOME_SCREEN
                        )
                    }
                }
            )
        }
    }
}

/* Screen composables. */

@Composable
private fun WallpaperTargetDialog(
    onDismiss: () -> Unit,
    onSelected: (WallpaperTarget) -> Unit
) {
    val color =
        ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)

    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = "Dismiss") }
        },
        confirmButton = {},
        title = { Text(text = "Set as background") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
            ) {
                WallpaperTarget.entries.forEach { target ->
                    SegmentedListItem(
                        onClick = { onSelected(target) },
                        colors = color,
                        shapes = ListItemDefaults.segmentedShapes(
                            index = target.ordinal,
                            count = 3
                        )
                    ) {
                        Text(
                            text = target.label,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun PhotoHeader(
    modifier: Modifier = Modifier,
    image: String,
    contentDescription: String,
    width: Int,
    height: Int,
    hashPainter: String,
    onClick: () -> Unit
) {
    val ratio = if (width > 0 && height > 0) width.toFloat() / height.toFloat() else 1f
    val blurHashPainter = BlurHashPainter(
        blurHash = hashPainter,
        width = width,
        height = height,
        punch = 0.7f,
        scale = 0.1f
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = image,
            contentDescription = contentDescription,
            placeholder = blurHashPainter,
            error = blurHashPainter,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(ratio)
                .clip(RoundedCornerShape(20.dp))
        )
    }
}

@Composable
private fun ImageZoomItem(
    photo: String,
    backGroundColor: Color
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backGroundColor),
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
            model = photo,
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
private fun UserInfoItem(
    profileImage: String,
    name: String,
    userName: String,
    color: String
) {
    val placeholderColor = ColorPainter(
        color = if (color.isEmpty()) Color.DarkGray else Color(color.toColorInt())
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AsyncImage(
            model = profileImage,
            contentDescription = name,
            contentScale = ContentScale.Crop,
            placeholder = placeholderColor,
            error = placeholderColor,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape
                )
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = userName,
                fontWeight = FontWeight.Light
            )
        }

//        Spacer(modifier = Modifier.weight(1f))
//
//        Box(modifier = Modifier.padding(16.dp)) {
//            IconButton(onClick = { expanded = !expanded }) {
//                val icon = if (expanded) Icons.Default.MoreHoriz else Icons.Default.MoreVert
//                val iconDescription = if (expanded) "Hide options." else "Show options."
//
//                Icon(icon, iconDescription)
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                shape = RoundedCornerShape(10.dp)
//            ) {
//                DropdownMenuItem(
//                    onClick = onUserProfile,
//                    text = { Text(text = "User profile") },
//                    leadingIcon = { Icon(Icons.Default.AccountBox, "User profile.") }
//                )
//
//                HorizontalDivider()
//
//                DropdownMenuItem(
//                    onClick = onDownload,
//                    text = { Text(text = "Download") },
//                    leadingIcon = { Icon(Icons.Default.Download, "Download image.") }
//                )
//                DropdownMenuItem(
//                    onClick = onSetAsWallpaper,
//                    text = { Text(text = "Set as wallpaper") },
//                    leadingIcon = { Icon(Icons.Default.Wallpaper, "Set as wallpaper.") }
//                )
//
//                HorizontalDivider()
//
//                DropdownMenuItem(
//                    onClick = onShare,
//                    text = { Text(text = "Share") },
//                    leadingIcon = { Icon(Icons.Default.Share, "Share image.") }
//                )
//            }
//        }
    }
}

@Composable
private fun DescriptionItem(str: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "\"${str}\"",
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
private fun CameraInformationItem(
    exifMake: String,
    exifName: String,
    exifModel: String,
    exifFocalLength: String,
    exifExposureTime: String,
    exifAperture: String,
    exifIso: String,
    aspectRatio: String
) {
    val cameraInfoList = listOf(
        CameraInfo(supportingText = "Make", headlineText = exifMake, leadingIcon = R.drawable.verified),
        CameraInfo(supportingText = "Make name", headlineText = exifName, leadingIcon = R.drawable.label),
        CameraInfo(supportingText = "Model", headlineText = exifModel, leadingIcon = R.drawable.photo_camera),
        CameraInfo(supportingText = "Focal length", headlineText = exifFocalLength, leadingIcon = R.drawable.visibility),
        CameraInfo(supportingText = "Shutter Speed", headlineText = exifExposureTime, leadingIcon = R.drawable.shutter_speed),
        CameraInfo(supportingText = "Aperture", headlineText = exifAperture, leadingIcon = R.drawable.camera_apperture),
        CameraInfo(supportingText = "Iso", headlineText = exifIso, leadingIcon = R.drawable.exposure),
        CameraInfo(supportingText = "Dimensions", headlineText = aspectRatio, leadingIcon = R.drawable.aspect_ratio)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        cameraInfoList.forEach { item ->
            ListItem(
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                leadingContent = { Icon(painterResource(item.leadingIcon), item.headlineText) },
                supportingContent = { Text(text = item.supportingText) },
                content = { Text(text = item.headlineText) }
            )
        }
    }
}

private data class CameraInfo(
    val supportingText: String,
    val headlineText: String,
    val leadingIcon: Int
)

@Composable
private fun TagListItem(
    tags: List<String>,
    onClick: () -> Unit
) {
    FlowColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        maxItemsInEachColumn = 5,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        itemHorizontalAlignment = Alignment.CenterHorizontally
    ) {
        tags.forEach { item ->
            SuggestionChip(
                onClick = onClick,
                label = { Text(text = item) },
                shape = RoundedCornerShape(20.dp),
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

/* Preview composables. */

@Preview
@Composable
private fun PhotoHeaderPreview() {
    PhotoHeader(
        image = "",
        contentDescription = "",
        hashPainter = "",
        width = 1000,
        height = 1000,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun UserInfoPreview() {
    UserInfoItem(
        color = "#a6c0c0",
        profileImage = "",
        name = "Lorem Impsum",
        userName = "loremImpsum"
    )
}

@Preview(showBackground = true)
@Composable
private fun DescriptionPreview() {
    DescriptionItem(str = "a brown and white cow standing in a field")
}

@Preview(showBackground = true)
@Composable
private fun TagListPreview() {
    val tags = ('a'..'z').map { it.toString() }

    TagListItem(
        tags = tags,
        onClick = {}
    )
}
@Preview(showBackground = true)
@Composable
private fun CameraInfoPreview() {
    CameraInformationItem(
        exifModel = "Canon EOS R5",
        exifExposureTime = "1/125s",
        exifAperture = "f/8.0",
        exifFocalLength = "50mm",
        exifIso = "100",
        aspectRatio = "5760 x 3840",
        exifMake = "NIKON CORPORATION",
        exifName = "NIKON CORPORATION, NIKON ZR",
    )
}