@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.feature.collections.presentation

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.maxidev.wallsplash.feature.collections.presentation.state.CollectionUiState

@Composable
fun CollectionScreen(
    viewModel: CollectionsViewModel = hiltViewModel(),
    popBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ScreenContent(
        uiState = state,
        popBack = popBack,
        navigateToDetail = navigateToDetail
    )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            state = rememberLazyListState(),
            contentPadding = innerPadding
        ) {
            item {
                CuratedUserItem(
                    user = collectionData?.name.orEmpty(),
                    totalPhotos = collectionData?.totalPhotos.orEmpty(),
                    link = collectionData?.link.orEmpty()
                )
            }
            items(
                collectionPhotos.itemCount,
                collectionPhotos.itemKey { it.photoId }
            ) { index ->
                val photos = collectionPhotos[index]

                if (photos != null) {
                    PhotoItem(
                        image = photos.urlRegular,
                        navigateToDetail = { navigateToDetail(photos.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PhotoItem(
    image: String,
    navigateToDetail: () -> Unit
) {
    Box (
        modifier = Modifier
            .padding(8.dp)
            .clickable { navigateToDetail() }
    ){
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(10.dp))
        )
    }
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
            Icon(
                Icons.AutoMirrored.Filled.OpenInNew,
                "View in browser."
            )
        }
    }
}

@[Composable Preview]
private fun CuratedUserPreview() {
    CuratedUserItem(
        user = "Collection by Lorem impsum",
        totalPhotos = "100 photos",
        link = "link"
    )
}