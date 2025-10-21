@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.feature.photos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.common.presentation.components.CustomAsyncImage
import com.maxidev.wallsplash.common.utils.handlePagingLoadState
import com.maxidev.wallsplash.feature.navigation.Destinations
import com.maxidev.wallsplash.feature.photos.presentation.model.CollectionsUi
import com.maxidev.wallsplash.feature.photos.presentation.model.PhotosUi
import com.maxidev.wallsplash.feature.photos.presentation.state.PhotosUiState
import com.wajahatiqbal.blurhash.BlurHashPainter

/* Extension that encapsulates the navigation code. */
fun NavGraphBuilder.photosDestination(
    navigateToPhotoDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToSettings: () -> Unit
) {
    composable<Destinations.PhotosView> {
        val viewModel = hiltViewModel<PhotosViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        ScreenContent(
            uiState = state,
            uiEvents = { events ->
                when (events) {
                    PhotosUiEvents.NavigateToFavorites -> { navigateToFavorites() }
                    PhotosUiEvents.NavigateToSearch -> { navigateToSearch() }
                    PhotosUiEvents.NavigateToSettings -> { navigateToSettings() }
                    PhotosUiEvents.OnRefresh -> { viewModel.refreshAll() }
                    is PhotosUiEvents.NavigateToPhotoDetails -> {
                        navigateToPhotoDetail(events.id)
                    }
                    is PhotosUiEvents.NavigateToCollectionDetails -> {
                        navigateToCollections(events.id)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: PhotosUiState,
    uiEvents: (PhotosUiEvents) -> Unit
) {
    val allPhotos = uiState.photos.collectAsLazyPagingItems()
    val allCollections = uiState.collections.collectAsLazyPagingItems()
    val isRefreshing = uiState.isRefreshing

    // Component state.
    val pullToRefreshState = rememberPullToRefreshState()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = listOf("Home", "Collections")

    // Separate states for the LazyList to avoid sharing state when switching tabs.
    val photosLazyState = rememberLazyStaggeredGridState()
    val collectionLazyState = rememberLazyStaggeredGridState()

    Scaffold(
        topBar = {
            PrimaryTabRow(
                modifier = Modifier.statusBarsPadding(),
                selectedTabIndex = selectedTabIndex
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(70.dp),
                actions = {
                    IconButton(onClick = { uiEvents(PhotosUiEvents.NavigateToSettings) }) {
                        Icon(Icons.Default.Settings, "Settings.")
                    }
                    IconButton(onClick = { uiEvents(PhotosUiEvents.NavigateToFavorites) }) {
                        Icon(Icons.Default.Favorite, "Favorites.")
                    }
                    IconButton(onClick = { uiEvents(PhotosUiEvents.NavigateToSearch) }) {
                        Icon(Icons.Default.Search, "Search.")
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            isRefreshing = isRefreshing,
            onRefresh = { uiEvents(PhotosUiEvents.OnRefresh) },
            state = pullToRefreshState
        ) {
            val currentState = if (selectedTabIndex == 0) photosLazyState else collectionLazyState

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding),
                columns = StaggeredGridCells.Adaptive(260.dp),
                state = currentState
            ) {
                when (selectedTabIndex) {
                    0 -> {
                        items(
                            count = allPhotos.itemCount,
                            key = allPhotos.itemKey { key -> key.id }
                        ) { index ->
                            val content = allPhotos[index]

                            if (content != null) {
                                PhotoItem(
                                    model = content,
                                    navigateToPhotoDetail = {
                                        uiEvents(
                                            PhotosUiEvents.NavigateToPhotoDetails(content.id)
                                        )
                                    }
                                )
                            }
                        }
                        handlePagingLoadState(
                            loadState = allPhotos.loadState,
                            itemCount = allPhotos.itemCount
                        )
                    }
                    1 -> {
                        items(
                            count = allCollections.itemCount,
                            key = allCollections.itemKey { key -> key.id }
                        ) { index ->
                            val content = allCollections[index]

                            if (content != null) {
                                CollectionItem(
                                    model = content,
                                    onClick = {
                                        uiEvents(
                                            PhotosUiEvents.NavigateToCollectionDetails(content.id)
                                        )
                                    }
                                )
                            }
                        }
                        handlePagingLoadState(
                            loadState = allCollections.loadState,
                            itemCount = allCollections.itemCount
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserItem(image: String, user: String) {
    val profileColorPainter = ColorPainter(Color.DarkGray)
    val roundedCornerShape = RoundedCornerShape(10.dp)

    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        AsyncImage(
            model = image,
            contentDescription = user,
            placeholder = profileColorPainter,
            error = profileColorPainter,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(roundedCornerShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = roundedCornerShape
                )
        )
        Spacer(Modifier.size(16.dp))
        Text(
            text = user,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun PhotoItem(
    model: PhotosUi,
    navigateToPhotoDetail: () -> Unit
) {
    val modPadding = 8.dp
    val roundedCornerShape = RoundedCornerShape(10.dp)
    val blurHolder = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    Column(
        modifier = Modifier
            .padding(modPadding)
            .clickable { navigateToPhotoDetail() },
        verticalArrangement = Arrangement.spacedBy(modPadding)
    ) {
        UserItem(
            image = model.profileImageLarge,
            user = model.name
        )
        CustomAsyncImage(
            modifier = Modifier
                .aspectRatio(model.width.toFloat() / model.height.toFloat())
                .padding(modPadding)
                .clip(roundedCornerShape),
            model = model.urlRegular,
            blurHash = blurHolder,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Composable
private fun CollectionItem(
    model: CollectionsUi,
    onClick: () -> Unit
) {
    val roundedCornerShape = RoundedCornerShape(10.dp)
    val colorWhite = Color.White
    val modPadding = 8.dp
    val blurHolder = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    Column(
        modifier = Modifier
            .padding(modPadding)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(modPadding)
    ) {
        UserItem(
            image = model.profileImageLarge,
            user = model.name
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(modPadding)
                .background(color = colorWhite, shape = roundedCornerShape)
        ) {
            CustomAsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .clip(roundedCornerShape),
                model = model.urlRegular,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                blurHash = blurHolder
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(modPadding),
                verticalArrangement = Arrangement.spacedBy(modPadding)
            ) {
                Text(
                    text = model.title,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = colorWhite
                )
                Text(
                    text = model.totalPhotos,
                    fontWeight = FontWeight.Light,
                    color = colorWhite
                )
            }
        }
    }
}

/* Preview composables. */

@Preview(showBackground = true)
@Composable
private fun UserItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        UserItem(
            image = "",
            user = "User Impsum"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PhotoItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        PhotoItem(
            model = PhotosUi(
                id = "id",
                name = "Userimpsum",
                profileImageLarge = "image",
                urlRegular = "image",
                blurHash = "blurhash",
                width = 100,
                height = 100,
                urlFull = ""
            ),
            navigateToPhotoDetail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        CollectionItem(
            model = CollectionsUi(
                id = "12345",
                title = "Awesome Collection Title",
                totalPhotos = "150 photos",
                width = 200,
                height = 200,
                blurHash = "",
                urlRegular = "url",
                name = "John Doe",
                profileImageLarge = "image"
            ),
            onClick = {}
        )
    }
}