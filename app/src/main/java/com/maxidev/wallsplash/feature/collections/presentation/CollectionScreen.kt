@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.feature.collections.presentation

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.maxidev.wallsplash.common.presentation.components.CustomAsyncImage
import com.maxidev.wallsplash.common.utils.handlePagingLoadState
import com.maxidev.wallsplash.feature.collections.presentation.model.CollectionPhotosUi
import com.maxidev.wallsplash.feature.collections.presentation.state.CollectionUiState
import com.maxidev.wallsplash.feature.navigation.Destinations
import com.wajahatiqbal.blurhash.BlurHashPainter

// TODO: Manage load states.

/* Extension that encapsulates the navigation code. */
fun NavGraphBuilder.collectionsDestination(
    popBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    composable<Destinations.CollectionsView> {
        val viewModel = hiltViewModel<CollectionsViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        ScreenContent(
            uiState = state,
            popBack = popBack,
            navigateToDetail = navigateToDetail
        )
    }
}

@Composable
private fun ScreenContent(
    uiState: CollectionUiState,
    popBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val collectionPhotos = uiState.collectionPhoto.collectAsLazyPagingItems()
    val collectionData = uiState.collectionData

    // Top bar variables
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    // Lazy list variables
    val lazyState = rememberLazyStaggeredGridState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = collectionData?.title.orEmpty()) },
                navigationIcon = {
                    IconButton(onClick = popBack) {
                        Icon(Icons.Default.ArrowBackIosNew, "Back.")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            columns = StaggeredGridCells.Adaptive(160.dp),
            state = lazyState,
            contentPadding = innerPadding
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                if (collectionData != null) {
                    CuratedUserItem(
                        user = collectionData.name,
                        totalPhotos = collectionData.totalPhotos,
                        link = collectionData.link
                    )
                }
            }
            items(
                collectionPhotos.itemCount,
                collectionPhotos.itemKey { it.photoId }
            ) { index ->
                val photos = collectionPhotos[index]

                if (photos != null) {
                    PhotoItem(
                        model = photos,
                        navigateToDetail = { navigateToDetail(photos.id) }
                    )
                }
            }
            handlePagingLoadState(
                loadState = collectionPhotos.loadState,
                itemCount = collectionPhotos.itemCount
            )
        }
    }
}

@Composable
private fun PhotoItem(
    model: CollectionPhotosUi,
    navigateToDetail: () -> Unit
) {
    CustomAsyncImage(
        modifier = Modifier
            .aspectRatio(model.width.toFloat() / model.height.toFloat())
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { navigateToDetail() },
        model = model.urlRegular,
        blurHash =  BlurHashPainter(
            blurHash = model.blurHash,
            width = model.width,
            height = model.height,
            punch = 0.7f,
            scale = 0.1f
        ),
        contentDescription = null,
        contentScale = ContentScale.FillBounds
    )
}

@Composable
private fun CuratedUserItem(
    user: String,
    totalPhotos: String,
    link: String
) {
    val localContext = LocalContext.current
    val viewIntent = Intent.ACTION_VIEW
    val browserIntent = Intent(viewIntent, link.toUri())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$totalPhotos • $user",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        )
        Spacer(Modifier.size(8.dp))
        IconButton(
            onClick = { localContext.startActivity(browserIntent) },
            modifier = Modifier.size(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.OpenInNew, "View in browser.")
        }
    }
}

@Preview
@Composable
private fun CuratedUserPreview() {
    CuratedUserItem(
        user = "Collection by Lorem impsum",
        totalPhotos = "100 photos",
        link = "link"
    )
}