@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.feature.search.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.maxidev.wallsplash.common.presentation.components.CustomSearchBar
import com.maxidev.wallsplash.feature.search.presentation.state.SearchUiState

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val input by viewModel.input.collectAsStateWithLifecycle()

    ScreenContent(
        uiState = state,
        input = input,
        onInputChange = viewModel::onInputChange,
        onSearch = viewModel::searchResult,
        navigateToDetail = navigateToDetail
    )
}

@Composable
private fun ScreenContent(
    uiState: SearchUiState,
    input: String,
    onInputChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val search = uiState.searchPhotos.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CustomSearchBar(
                query = input,
                onQueryChange = onInputChange,
                onSearch = onSearch,
                content = { /* TODO: Do nothing for now. */ }
            )
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            columns = StaggeredGridCells.Adaptive(160.dp),
            state = rememberLazyStaggeredGridState(),
            contentPadding = innerPadding
        ) {
            items(
                count = search.itemCount,
                key = search.itemKey { key -> key.photoId }
            ) { index ->
                val pagingContent = search[index]

                if (pagingContent != null) {
                    ImageItem(
                        imageUrl = pagingContent.urlRegular,
                        onClick = { navigateToDetail(pagingContent.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageItem(
    imageUrl: String,
    onClick: () -> Unit
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clickable { onClick() }
    )
}

@[Composable Preview]
private fun ImageItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        ImageItem(
            imageUrl = "",
            onClick = {}
        )
    }
}