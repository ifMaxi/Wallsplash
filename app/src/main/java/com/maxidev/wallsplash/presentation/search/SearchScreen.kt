@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.wallsplash.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExpandedDockedSearchBarWithGap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSearchBarWithGapState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.domain.model.History
import com.maxidev.wallsplash.domain.model.SearchCollections
import com.maxidev.wallsplash.domain.model.SearchPhoto
import com.maxidev.wallsplash.utils.handlePagingLoadState
import com.wajahatiqbal.blurhash.BlurHashPainter
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val historyState by viewModel.historyState.collectAsStateWithLifecycle()
    val textField = viewModel.textFieldState

    ScreenContent(
        uiState = state,
        textFieldState = textField,
        onSearch = viewModel::onSearch,
        navigateToDetail = navigateToDetail,
        navigateToCollections = navigateToCollections,
        historyList = historyState,
        onSaveToDataBase = viewModel::saveInDb,
        onDelete = viewModel::deleteItem,
        onClearAll = viewModel::clearAll,
    )
}

@Composable
private fun ScreenContent(
    uiState: SearchUiState,
    historyList: List<String>,
    onSearch: (String) -> Unit,
    textFieldState: TextFieldState,
    onSaveToDataBase: (History) -> Unit,
    onDelete: (String) -> Unit,
    onClearAll: () -> Unit,
    navigateToDetail: (String) -> Unit,
    navigateToCollections: (String) -> Unit
) {
    val searchPhotos = uiState.searchPhotos.collectAsLazyPagingItems()
    val searchCollections = uiState.searchCollections.collectAsLazyPagingItems()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = listOf("Photos", "Collections")

    // Separate states for the LazyList to avoid sharing state when switching tabs.
    val photosLazyState = rememberLazyStaggeredGridState()
    val collectionLazyState = rememberLazyStaggeredGridState()
    val currentState = if (selectedTabIndex == 0) photosLazyState else collectionLazyState

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center
            ) {
                SearchBarItem(
                    textFieldState = textFieldState,
                    onSearch = onSearch,
                    scrollBehavior = scrollBehavior,
                    historyList = historyList,
                    onSaveToDataBase = onSaveToDataBase,
                    onDelete = onDelete,
                    onClearAll = onClearAll
                )
                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(160.dp),
            state = currentState,
            contentPadding = innerPadding
        ) {
            when (selectedTabIndex) {
                0 -> {
                    items(
                        count = searchPhotos.itemCount,
                        key = searchPhotos.itemKey { key -> key.photoId }
                    ) { index ->
                        val pagingContent = searchPhotos[index]

                        if (pagingContent != null) {
                            ImageItem(
                                model = pagingContent,
                                navigateToDetail = { navigateToDetail(pagingContent.id) }
                            )
                        }
                    }
                    handlePagingLoadState(
                        loadState = searchPhotos.loadState,
                        itemCount = searchPhotos.itemCount
                    )
                }

                1 -> {
                    items(
                        count = searchCollections.itemCount,
                        key = searchCollections.itemKey { key -> key.collectionId }
                    ) { index ->
                        val pagingContent = searchCollections[index]

                        if (pagingContent != null) {
                            CollectionItem(
                                model = pagingContent,
                                navigateToCollectionsDetail = { navigateToCollections(pagingContent.id) }
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

@Composable
private fun ImageItem(
    model: SearchPhoto,
    navigateToDetail: () -> Unit
) {
    val blurHashPlaceholder = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    AsyncImage(
        model = model.urlRegular,
        contentDescription = null,
        placeholder = blurHashPlaceholder,
        error = blurHashPlaceholder,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(model.width.toFloat() / model.height.toFloat())
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { navigateToDetail() }
    )
}

@Composable
private fun CollectionItem(
    model: SearchCollections,
    navigateToCollectionsDetail: () -> Unit
) {
    val roundedCornerShape = RoundedCornerShape(20.dp)
    val colorWhite = Color.White
    val blurHashPainter = BlurHashPainter(
        blurHash = model.blurHash,
        width = model.width,
        height = model.height,
        punch = 0.7f,
        scale = 0.1f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(color = colorWhite, shape = roundedCornerShape)
            .clickable { navigateToCollectionsDetail() }
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .clip(roundedCornerShape),
            model = model.coverPhoto,
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

@Composable
private fun SearchBarItem(
    historyList: List<String>,
    textFieldState: TextFieldState = rememberTextFieldState(),
    onSearch: (String) -> Unit,
    scrollBehavior: SearchBarScrollBehavior? = null,
    onSaveToDataBase: (History) -> Unit,
    onDelete: (String) -> Unit,
    onClearAll: () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val scope = rememberCoroutineScope()
    val searchBarState = rememberSearchBarWithGapState()
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                textFieldState = textFieldState,
                searchBarState = searchBarState,
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    onSaveToDataBase(History(query = textFieldState.text.toString()))
                    scope.launch { searchBarState.animateToCollapsed() }
                },
                placeholder = {
                    Text(modifier = Modifier.clearAndSetSemantics {}, text = "Search")
                },
                leadingIcon = {
                    Icon(painterResource(R.drawable.search), contentDescription = "Search")
                },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            modifier = Modifier.clickable { textFieldState.clearText() }
                        )
                    }
                }
            )
        }

    AppBarWithSearch(
        state = searchBarState,
        inputField = inputField,
        shape = shape,
        scrollBehavior = scrollBehavior
    )
    ExpandedDockedSearchBarWithGap(
        state = searchBarState,
        inputField = inputField,
        shape = shape,
        dropdownShape = shape
    ) {
        if (historyList.isEmpty()) {
            Text(
                text = "No recent searches",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn {
                item {
                    DeleteAllItem(onClearAll = onClearAll)
                }
                items(items = historyList, key = { it }) { item ->
                    ListItem(
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        leadingContent = { Icon(painterResource(R.drawable.history), "History") },
                        trailingContent = {
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(
                                    painterResource(R.drawable.close),
                                    contentDescription = "Delete"
                                )
                            }
                        },
                        content = { Text(text = item) },
                        onClick = {
                            textFieldState.setTextAndPlaceCursorAtEnd(item)
                            onSearch(textFieldState.text.toString())
                            scope.launch { searchBarState.animateToCollapsed() }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteAllItem(
    modifier: Modifier = Modifier,
    onClearAll: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent",
            fontSize = 14.sp
        )
        TextButton(onClick = onClearAll) {
            Text(text = "Clear all")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteAllPreview() {
    DeleteAllItem(
        onClearAll = {}
    )
}