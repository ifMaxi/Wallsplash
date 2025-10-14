@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.feature.photos.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.feature.photos.presentation.state.PhotosUiState

@Composable
fun PhotosScreen(
    viewModel: PhotosViewModel = hiltViewModel(),
    navigateToPhotoDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToSettings: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenContent(
        uiState = state,
        onRefresh = viewModel::refreshAll,
        navigateToPhotoDetail = navigateToPhotoDetail,
        navigateToCollections = navigateToCollections,
        navigateToSearch = navigateToSearch,
        navigateToFavorites = navigateToFavorites,
        navigateToSettings = navigateToSettings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
    uiState: PhotosUiState,
    onRefresh: () -> Unit,
    navigateToPhotoDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToFavorites: () -> Unit,
    navigateToSettings: () -> Unit
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

    // TODO: Apply loading/error state.
    // TODO: Apply OnClick methods.

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
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings."
                        )
                    }
                    IconButton(onClick = navigateToFavorites) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites."
                        )
                    }
                    IconButton(onClick = navigateToSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search."
                        )
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
            onRefresh = onRefresh,
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
                                    url = content.urlRegular,
                                    name = content.name,
                                    profileImageLarge = content.profileImageLarge,
                                    onClick = { navigateToPhotoDetail(content.id) }
                                )
                            }
                        }
                    }
                    1 -> {
                        items(
                            count = allCollections.itemCount,
                            key = allCollections.itemKey { key -> key.id }
                        ) { index ->
                            val content = allCollections[index]

                            if (content != null) {
                                CollectionItem(
                                    imageUrl = content.urlRegular,
                                    title = content.title,
                                    totalPhotos = content.totalPhotos,
                                    name = content.name,
                                    profileImage = content.profileImageLarge,
                                    onClick = { navigateToCollections(content.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoItem(
    url: String,
    name: String,
    profileImageLarge: String,
    onClick: () -> Unit
) {
    val paddingSpace = 8.dp

    // TODO: Apply loading color effect to images.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingSpace)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(paddingSpace)
    ) {
        Row(
            modifier = Modifier.padding(paddingSpace),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(paddingSpace)
        ) {
            AsyncImage(
                model = profileImageLarge,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
                fontSize = 14.sp
            )
        }
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingSpace)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@[Composable Preview]
private fun PhotoItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        PhotoItem(
            url = "Image",
            name = "Lorem Impsum",
            profileImageLarge = "Image",
            onClick = {}
        )
    }
}

@Composable
private fun CollectionItem(
    imageUrl: String,
    title: String,
    totalPhotos: String,
    name: String,
    profileImage: String,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    val colorWhite = Color.White
    val paddingSpace = 8.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingSpace)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(paddingSpace)
    ) {
        Row(
            modifier = Modifier.padding(paddingSpace),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(paddingSpace)
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Text(
                text = name,
                fontSize = 14.sp
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingSpace)
                .background(color = colorWhite, shape = shape)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(shape)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(paddingSpace),
                verticalArrangement = Arrangement.spacedBy(paddingSpace)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = colorWhite
                )
                Text(
                    text = totalPhotos,
                    fontWeight = FontWeight.Light,
                    color = colorWhite
                )
            }
        }
    }
}

@[Composable Preview]
private fun CollectionItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        CollectionItem(
            imageUrl = "image",
            title = "Lorem impsum.",
            totalPhotos = "Total photos: 30",
            name = "Userimpsum",
            profileImage = "image",
            onClick = {}
        )
    }
}