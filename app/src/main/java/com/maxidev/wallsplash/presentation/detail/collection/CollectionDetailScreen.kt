package com.maxidev.wallsplash.presentation.detail.collection

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.domain.model.CollectionData
import com.maxidev.wallsplash.domain.model.CollectionPhotos
import com.maxidev.wallsplash.presentation.components.CustomNetworkErrorItem
import com.maxidev.wallsplash.presentation.components.CustomNetworkLoadingItem
import com.maxidev.wallsplash.utils.handlePagingLoadState
import com.wajahatiqbal.blurhash.BlurHashPainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun CollectionDetailScreen(
    viewModel: CollectionDetailViewModel,
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoadContent(
        uiState = state,
        navigateToDetail = navigateToDetail
    )
}

@Composable
private fun LoadContent(
    uiState: CollectionUiState,
    navigateToDetail: (String) -> Unit
) {
    when {
        uiState.isLoading -> {
            CustomNetworkLoadingItem()
        }
        uiState.isError != null -> {
            CustomNetworkErrorItem(
                message = uiState.isError,
                onRetry = {}
            )
        }
        else -> {
            ScreenContent(
                data = uiState.collectionData ?: return,
                photos = uiState.collectionPhoto,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
private fun ScreenContent(
    data: CollectionData,
    photos: Flow<PagingData<CollectionPhotos>>,
    navigateToDetail: (String) -> Unit
) {
    val pagingItems = photos.collectAsLazyPagingItems()
    val lazyState = rememberLazyStaggeredGridState()
    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        scope.launch {
            delay(2.seconds)
            pagingItems.refresh()
            isRefreshing = false
        }
    }
    val showButton by remember {
        derivedStateOf { lazyState.firstVisibleItemIndex > 10 }
    }

    Scaffold(
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
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize(),
                columns = StaggeredGridCells.Adaptive(160.dp),
                state = lazyState,
                contentPadding = innerPadding
            ) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    CuratedUserItem(
                        title = data.title,
                        user = data.name,
                        totalPhotos = data.totalPhotos,
                        link = data.link
                    )
                }
                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.photoId }
                ) { index ->
                    pagingItems[index]?.let {
                        PhotoItem(
                            model = it,
                            navigateToDetail = { navigateToDetail(it.id) }
                        )
                    }
                }
                handlePagingLoadState(
                    loadState = pagingItems.loadState,
                    itemCount = pagingItems.itemCount,
                    onRetry = { pagingItems.retry() }
                )
            }
        }
    }
}

@Composable
private fun PhotoItem(
    model: CollectionPhotos,
    navigateToDetail: () -> Unit
) {
    val hashPainter = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    AsyncImage(
        modifier = Modifier
            .aspectRatio(model.width.toFloat() / model.height.toFloat())
            .padding(8.dp)
            .dropShadow(
                shape = RoundedCornerShape(20.dp),
                shadow = Shadow(
                    radius = 6.dp,
                    color = Color.Black.copy(alpha = 0.3f),
                    spread = 2.dp,
                    offset = DpOffset(2.dp, 2.dp)
                )
            )
            .clip(RoundedCornerShape(20.dp))
            .clickable { navigateToDetail() },
        model = model.urlFull,
        placeholder = hashPainter,
        error = hashPainter,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun CuratedUserItem(
    title: String,
    user: String,
    totalPhotos: String,
    link: String
) {
    val localContext = LocalContext.current
    val viewIntent = Intent.ACTION_VIEW
    val browserIntent = Intent(viewIntent, link.toUri())

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$totalPhotos • $user",
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.size(8.dp))
            IconButton(
                onClick = { localContext.startActivity(browserIntent) },
                modifier = Modifier.size(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.OpenInNew, "View in browser.")
            }
        }
        HorizontalDivider()
    }
}

@Preview(showBackground = true)
@Composable
private fun CuratedUserPreview() {
    CuratedUserItem(
        title = "Lorem impsum",
        user = "Collection by Lorem impsum",
        totalPhotos = "100 photos",
        link = "link"
    )
}