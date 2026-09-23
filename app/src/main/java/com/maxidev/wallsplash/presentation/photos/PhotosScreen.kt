@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.presentation.photos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.domain.model.Photos
import com.maxidev.wallsplash.presentation.components.UserItem
import com.maxidev.wallsplash.utils.handlePagingLoadState
import com.wajahatiqbal.blurhash.BlurHashPainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun PhotosScreen(
    viewModel: PhotosViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit,
    navigateToUserDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenContent(
        uiState = state,
        navigateToDetail = navigateToDetail,
        navigateToUserDetail = navigateToUserDetail
    )
}

@Composable
private fun ScreenContent(
    uiState: PhotosUiState,
    navigateToDetail: (String) -> Unit,
    navigateToUserDetail: (String) -> Unit
) {
    val allPhotos = uiState.photos.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true

        scope.launch {
            delay(2.seconds)
            allPhotos.refresh()
            isRefreshing = false
        }
    }
    val photosLazyState = rememberLazyStaggeredGridState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Wallsplash") }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = StaggeredGridCells.Adaptive(260.dp),
                state = photosLazyState,
                contentPadding = innerPadding
            ) {
                items(
                    count = allPhotos.itemCount,
                    key = allPhotos.itemKey { key -> key.id }
                ) { index ->
                    val content = allPhotos[index]

                    if (content != null) {
                        PhotoItem(
                            model = content,
                            navigateToPhotoDetail = { navigateToDetail(content.id) },
                            navigateToUserDetail = { navigateToUserDetail(content.userId)}
                        )
                    }
                }
                handlePagingLoadState(
                    loadState = allPhotos.loadState,
                    itemCount = allPhotos.itemCount
                )
//                items(
//                    count = allCollections.itemCount,
//                    key = allCollections.itemKey { key -> key.id }
//                ) { index ->
//                    val content = allCollections[index]
//
//                    if (content != null) {
//                        CollectionItem(
//                            model = content,
//                            onClick = {
//                                uiEvents(
//                                    PhotosUiEvents.NavigateToCollectionDetails(content.id)
//                                )
//                            }
//                        )
//                    }
//                }
//                handlePagingLoadState(
//                    loadState = allCollections.loadState,
//                    itemCount = allCollections.itemCount
//                )
            }
        }
    }
}

@Composable
private fun PhotoItem(
    model: Photos,
    navigateToPhotoDetail: () -> Unit,
    navigateToUserDetail: () -> Unit
) {
    val blurHashPainter = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserItem(
            image = model.profileImageLarge,
            user = model.name,
            navigateToUserDetail = navigateToUserDetail
        )
        AsyncImage(
            modifier = Modifier
                .aspectRatio(model.width.toFloat() / model.height.toFloat())
                .padding(8.dp)
                .clip(RoundedCornerShape(20.dp))
                .clickable { navigateToPhotoDetail() },
            model = model.urlRegular,
            placeholder = blurHashPainter,
            error = blurHashPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

/* Preview composables. */

@Preview(showBackground = true)
@Composable
private fun PhotoItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        PhotoItem(
            model = Photos(
                id = "id",
                name = "Userimpsum",
                profileImageLarge = "image",
                urlRegular = "image",
                blurHash = "blurhash",
                width = 100,
                height = 100,
                urlFull = "",
                userId = ""
            ),
            navigateToPhotoDetail = {},
            navigateToUserDetail = {}
        )
    }
}