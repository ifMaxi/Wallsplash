package com.maxidev.wallsplash.presentation.collections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.domain.model.Collections
import com.maxidev.wallsplash.presentation.components.UserItem
import com.maxidev.wallsplash.utils.handlePagingLoadState
import com.wajahatiqbal.blurhash.BlurHashPainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun CollectionsScreen(
    viewModel: CollectionsViewModel = hiltViewModel(),
    navigateToCollectionDetail: (String) -> Unit,
    navigateToUserDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    ScreenContent(
        uiState = state,
        navigateToCollectionDetail = navigateToCollectionDetail,
        navigateToUserDetail = navigateToUserDetail
    )
}

@Composable
private fun ScreenContent(
    uiState: CollectionsUiState,
    navigateToCollectionDetail: (String) -> Unit,
    navigateToUserDetail: (String) -> Unit
) {
    val allCollections = uiState.collections.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    val lazyState = rememberLazyGridState()
    val topBarState = rememberTopAppBarState()
    val scrollState = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true

        scope.launch {
            delay(2.seconds)
            allCollections.refresh()
            isRefreshing = false
        }
    }
    val showButton by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 5 }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollState.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Collections") },
                scrollBehavior = scrollState
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showButton,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        scope.launch {
                            lazyState.animateScrollToItem(0)
                            onRefresh()
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_circle_up),
                        contentDescription = "Go to top."
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            state = pullToRefreshState
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = lazyState,
                contentPadding = innerPadding,
                columns = GridCells.Adaptive(260.dp)
            ) {
                items(
                    count = allCollections.itemCount,
                    key = allCollections.itemKey { key -> key.id }
                ) { index ->
                    allCollections[index]?.let {
                        CollectionItem(
                            model = it,
                            navigateToCollectionDetail = { navigateToCollectionDetail(it.id) },
                            navigateToUserDetail = { navigateToUserDetail(it.userId) }
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

@Composable
private fun CollectionItem(
    model: Collections,
    navigateToCollectionDetail: () -> Unit,
    navigateToUserDetail: () -> Unit
) {
    val roundedCornerShape = RoundedCornerShape(10.dp)
    val colorWhite = Color.White
    val blurHashPainter = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UserItem(
            image = model.profileImageLarge,
            user = model.name,
            navigateToUserDetail = navigateToUserDetail
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(color = colorWhite, shape = roundedCornerShape)
                .clickable { navigateToCollectionDetail() }
        ) {
            AsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .clip(roundedCornerShape),
                model = model.urlRegular,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = blurHashPainter,
                error = blurHashPainter
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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
                    color = colorWhite
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
private fun CollectionItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        CollectionItem(
            model = Collections(
                id = "12345",
                title = "Awesome Collection Title",
                totalPhotos = "150 photos",
                width = 200,
                height = 200,
                blurHash = "",
                urlRegular = "url",
                userId = "",
                name = "John Doe",
                profileImageLarge = "image"
            ),
            navigateToCollectionDetail = {},
            navigateToUserDetail = {},
        )
    }
}