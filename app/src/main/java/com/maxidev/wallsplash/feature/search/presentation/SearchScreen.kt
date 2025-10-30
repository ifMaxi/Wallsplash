@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.feature.search.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.common.presentation.components.CustomAsyncImage
import com.maxidev.wallsplash.common.presentation.components.CustomSearchBar
import com.maxidev.wallsplash.common.utils.handlePagingLoadState
import com.maxidev.wallsplash.feature.navigation.Destinations
import com.maxidev.wallsplash.feature.search.presentation.model.SearchCollectionUi
import com.maxidev.wallsplash.feature.search.presentation.model.SearchPhotoUi
import com.maxidev.wallsplash.feature.search.presentation.state.SearchUiState
import com.wajahatiqbal.blurhash.BlurHashPainter
import java.util.UUID

/* Extension that encapsulates the navigation code. */
fun NavGraphBuilder.searchDestination(
    navigateToDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit
) {
    composable<Destinations.SearchView> {
        val viewModel = hiltViewModel<SearchViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val input by viewModel.input.collectAsStateWithLifecycle()

        ScreenContent(
            uiState = state,
            input = input,
            onInputChange = viewModel::onInputChange,
            onSearch = viewModel::onSearch,
            navigateToDetail = navigateToDetail,
            navigateToCollections = navigateToCollections
        )
    }
}

@Composable
private fun ScreenContent(
    uiState: SearchUiState,
    input: String,
    onInputChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit
) {
    val searchPhotos = uiState.searchPhotos.collectAsLazyPagingItems()
    val searchCollections = uiState.searchCollections.collectAsLazyPagingItems()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = listOf("Photos", "Collections")

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
            CustomSearchBar(
                modifier = Modifier.navigationBarsPadding(),
                query = input,
                onQueryChange = onInputChange,
                onSearch = onSearch,
                content = { /* DO NOTHING ! */ }
            )
        }
    ) { innerPadding ->
        val currentState = if (selectedTabIndex == 0) photosLazyState else collectionLazyState

        Box(modifier = Modifier.statusBarsPadding()) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding),
                columns = StaggeredGridCells.Adaptive(160.dp),
                state = currentState,
                contentPadding = innerPadding
            ) {
                when (selectedTabIndex) {
                    0 -> {
                        val searchPhotosItemCount = searchPhotos.itemCount

                        if (searchPhotosItemCount == 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                IdleMessageItem()
                            }
                        } else {
                            items(
                                count = searchPhotosItemCount,
                                key = searchPhotos.itemKey { key -> key.photoId }
                            ) { index ->
                                val pagingContent = searchPhotos[index]

                                if (pagingContent != null) {
                                    ImageItem(
                                        model = pagingContent,
                                        onClick = { navigateToDetail(pagingContent.id) }
                                    )
                                }
                            }
                            handlePagingLoadState(
                                loadState = searchPhotos.loadState,
                                itemCount = searchPhotos.itemCount
                            )
                        }
                    }

                    1 -> {
                        val searchCollectionsItemCount = searchCollections.itemCount

                        if (searchCollectionsItemCount == 0) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                IdleMessageItem()
                            }
                        } else {
                            items(
                                count = searchCollectionsItemCount,
                                key = searchCollections.itemKey { key -> key.collectionId }
                            ) { index ->
                                val pagingContent = searchCollections[index]

                                if (pagingContent != null) {
                                    CollectionItem(
                                        model = pagingContent,
                                        onClick = { navigateToCollections(pagingContent.id) }
                                    )
                                }
                            }
                            handlePagingLoadState(
                                loadState = searchCollections.loadState,
                                itemCount = searchCollections.itemCount
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageItem(
    model: SearchPhotoUi,
    onClick: () -> Unit
) {
    val blurHolder = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    CustomAsyncImage(
        model = model.urlRegular,
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        blurHash = blurHolder,
        modifier = Modifier
            .aspectRatio(model.width.toFloat() / model.height.toFloat())
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    )
}

@Composable
private fun CollectionItem(
    model: SearchCollectionUi,
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(modPadding)
            .background(color = colorWhite, shape = roundedCornerShape)
            .clickable { onClick() }
    ) {
        CustomAsyncImage(
            model = model.coverPhoto,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            blurHash = blurHolder,
            modifier = Modifier
                .height(200.dp)
                .clip(roundedCornerShape)
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

@Composable
private fun IdleMessageItem() {
    val isSystemTheme = isSystemInDarkTheme()
    val tintColor = MaterialTheme.colorScheme.outline
    val changeTint = if (isSystemTheme) tintColor else tintColor
    val modPadding = 8.dp

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.searching_image),
            contentDescription = "Search image.",
            colorFilter = ColorFilter.tint(changeTint),
            modifier = Modifier
                .padding(modPadding)
                .size(120.dp)
        )
        Spacer(modifier = Modifier.padding(modPadding))
        Text(
            text = "Nothing to see here,\ntry to find an image.",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(modPadding)
        )
    }
}

/* Preview composables. */

@Preview
@Composable
private fun ImageItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        ImageItem(
            model = SearchPhotoUi(
                photoId = UUID.randomUUID(),
                id = "1",
                width = 100,
                height = 100,
                blurHash = "",
                urlRegular = ""
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun IdleMessagePreview() {
    IdleMessageItem()
}