@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.presentation.detail.users

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuGroup
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenuPopup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.domain.model.User
import com.maxidev.wallsplash.domain.model.UserCollections
import com.maxidev.wallsplash.domain.model.UserPhoto
import com.wajahatiqbal.blurhash.BlurHashPainter
import kotlinx.coroutines.flow.Flow

@Composable
fun UserScreen(
    viewModel: UserViewModel,
    navigateToDetail: (String) -> Unit,
    navigateToCollectionsDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoadStateScreen(
        uiState = state,
        navigateToDetail = navigateToDetail,
        navigateToCollectionsDetail = navigateToCollectionsDetail
    )
}

@Composable
private fun LoadStateScreen(
    uiState: UserUiState,
    navigateToDetail: (String) -> Unit,
    navigateToCollectionsDetail: (String) -> Unit
) {
    when {
        uiState.isLoading -> {}
        uiState.isError != null -> {}
        else -> {
            ScreenContent(
                user = uiState.user ?: return,
                photos = uiState.userPhotos,
                collections = uiState.userCollections,
                navigateToDetail = navigateToDetail,
                navigateToCollectionsDetail = navigateToCollectionsDetail
            )
        }
    }
}

@Composable
private fun ScreenContent(
    user: User,
    photos: Flow<PagingData<UserPhoto>>,
    collections: Flow<PagingData<UserCollections>>,
    navigateToDetail: (String) -> Unit,
    navigateToCollectionsDetail: (String) -> Unit
) {
    val photos = photos.collectAsLazyPagingItems()
    val collections = collections.collectAsLazyPagingItems()
    val lazyState = rememberLazyStaggeredGridState()
    var selectedTabIndex by remember { mutableStateOf(Tabs.PHOTOS) }

    LaunchedEffect(selectedTabIndex) {
        if (lazyState.firstVisibleItemIndex > 1) {
            lazyState.scrollToItem(0)
        }
    }

    Scaffold { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize(),
            columns = StaggeredGridCells.Adaptive(160.dp),
            state = lazyState,
            contentPadding = innerPadding
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                UserHeader(
                    name = user.name,
                    bio = user.bio,
                    profileImage = user.profileImage,
                    location = user.location,
                    portfolioUrl = user.portfolioUrl,
                    instagramUser = user.instagramUsername,
                    twitterUser = user.twitterUsername,
                    userLink = user.userHtml
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                InterestItem(tags = user.tags.tag)
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                FeaturedPhotos(
                    featured = user.photos,
                    onNavigateToDetail = navigateToDetail
                )
            }
            item(key = "tabs", span = StaggeredGridItemSpan.FullLine) {
                PrimaryTabRow(selectedTabIndex = selectedTabIndex.ordinal) {
                    Tabs.entries.forEach { tab ->
                        Tab(
                            selected = tab == selectedTabIndex,
                            text = { Text(text = tab.name) },
                            onClick = { selectedTabIndex = tab }
                        )
                    }
                }
            }

            when (selectedTabIndex) {
                Tabs.PHOTOS -> {
                    items(
                        count = photos.itemCount,
                        key = photos.itemKey { it.photoId }
                    ) { index ->
                        photos[index]?.let {
                            ImageItem(
                                model = it,
                                navigateToDetail = { navigateToDetail(it.id) }
                            )
                        }
                    }
                }
                Tabs.COLLECTIONS -> {
                    items(
                        count = collections.itemCount,
                        key = collections.itemKey { it.collectionId }
                    ) { index ->
                        collections[index]?.let {
                            CollectionItem(
                                model = it,
                                navigateToCollectionsDetail = { navigateToCollectionsDetail(it.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class Tabs {
    PHOTOS, COLLECTIONS
}

@Composable
private fun UserHeader(
    name: String,
    bio: String,
    profileImage: String,
    location: String,
    portfolioUrl: String,
    instagramUser: String,
    twitterUser: String,
    userLink: String
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .dropShadow(
                        shape = CircleShape,
                        shadow = Shadow(
                            radius = 6.dp,
                            color = Color.Black.copy(alpha = 0.3f),
                            spread = 2.dp,
                            offset = DpOffset(2.dp, 2.dp)
                        )
                    )
                    .clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = location.ifEmpty { "Unspecified location." },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            val socialItems = remember(twitterUser, instagramUser, portfolioUrl, userLink) {
                buildList {
                    twitterUser.takeIf { it.isNotBlank() }?.let {
                        add(
                            SocialItem(
                                label = "X/(Twitter)", icon = R.drawable.x_icon,
                                webUrl = "https://twitter.com/$it", appPackage = "com.twitter.android"
                            )
                        )
                    }
                    instagramUser.takeIf { it.isNotBlank() }?.let {
                        add(
                            SocialItem(
                                label = "Instagram", icon = R.drawable.instagram_icon,
                                webUrl = "https://instagram.com/$it",
                                appPackage = "com.instagram.android",
                                appUrl = "https://instagram.com/_u/$it"
                            )
                        )
                    }
                    portfolioUrl.takeIf { it.isNotBlank() }?.let {
                        add(SocialItem("Portfolio", R.drawable.work, it))
                    }
                    userLink.takeIf { it.isNotBlank() }?.let {
                        add(SocialItem("Unsplash profile", R.drawable.link, it))
                    }
                }
            }

            if (socialItems.isNotEmpty()) {
                Box(contentAlignment = Alignment.TopCenter) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(painterResource(R.drawable.person_book), null)
                    }
                    DropdownMenuPopup(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        DropdownMenuGroup(
                            shapes = MenuDefaults.groupShape(0, 1),
                            interactionSource = interactionSource
                        ) {
                            MenuDefaults.DropdownMenuGroupLabel {
                                Text(text = "Socials", fontWeight = FontWeight.Medium)
                            }
                            HorizontalDivider(
                                Modifier.padding(MenuDefaults.HorizontalDividerPadding)
                            )
                            socialItems.fastForEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item.label) },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(item.icon),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                    },
                                    onClick = {
                                        expanded = false
                                        context.openLink(
                                            webUrl = item.webUrl,
                                            appPackage = item.appPackage,
                                            appUrl = item.appUrl
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        if (bio.isNotBlank() || bio.isNotEmpty()) {
            Text(
                text = bio,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

private data class SocialItem(
    val label: String,
    @DrawableRes val icon: Int,
    val webUrl: String,
    val appPackage: String? = null,
    val appUrl: String = webUrl
)

private fun Context.openLink(
    webUrl: String,
    appPackage: String? = null,
    appUrl: String = webUrl
) {
    val actionView = Intent.ACTION_VIEW

    if (appPackage != null) {
        val appIntent = Intent(actionView, appUrl.toUri()).apply {
            setPackage(appPackage)
        }

        try {
            startActivity(appIntent)
        } catch (_: ActivityNotFoundException) {}
    }

    val browserIntent = Intent(actionView, webUrl.toUri())
    try {
        startActivity(browserIntent)
    } catch (e: ActivityNotFoundException) {
        println(e.message)
    }
}

@Composable
private fun InterestItem(tags: List<String>) {
    if (tags.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Interest")
            FlexBox(
                modifier = Modifier.fillMaxWidth(),
                config = {
                    direction(FlexDirection.Row)
                    wrap(FlexWrap.Wrap)
                    justifyContent(FlexJustifyContent.Start)
                    gap(8.dp)
                }
            ) {
                tags.forEach { item ->
                    Text(text = item)
                }
            }
        }
    }
}

@Composable
private fun FeaturedPhotos(
    featured: List<User.Photos>,
    onNavigateToDetail: (String) -> Unit
) {
    val carouselState = rememberCarouselState { featured.count() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Featured",
            modifier = Modifier
                .align(Alignment.Start)
        )
        HorizontalMultiBrowseCarousel(
            modifier = Modifier.fillMaxWidth(),
            state = carouselState,
            preferredItemWidth = 186.dp,
            itemSpacing = 4.dp
        ) { index ->
            featured[index].let { userPhotos ->
                val placeholder = BlurHashPainter(
                    blurHash = userPhotos.blurHash,
                    width = 100, height = 100, punch = 0.7f, scale = 0.1f
                )

                AsyncImage(
                    model = userPhotos.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = placeholder,
                    error = placeholder,
                    modifier = Modifier
                        .aspectRatio(2f)
                        .maskClip(RoundedCornerShape(20.dp))
                        .clickable(onClick = { onNavigateToDetail(userPhotos.id) })
                )
            }
        }
    }
}

@Composable
private fun ImageItem(
    model: UserPhoto,
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
            .clickable { navigateToDetail() }
    )
}

@Composable
private fun CollectionItem(
    model: UserCollections,
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
                .dropShadow(
                    shape = roundedCornerShape,
                    shadow = Shadow(
                        radius = 6.dp,
                        color = Color.Black.copy(alpha = 0.3f),
                        spread = 2.dp,
                        offset = DpOffset(2.dp, 2.dp)
                    )
                )
                .clip(roundedCornerShape),
            model = model.coverPhoto,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = blurHashPainter,
            error = blurHashPainter
        )
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = model.title,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = colorWhite
            )
            Text(text = model.totalPhotos, color = colorWhite)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserHeaderPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.DarkGray.toArgb())
    }

    CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
        UserHeader(
            name = "Lorem Impsum",
            bio = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            profileImage = "",
            location = "Location",
            portfolioUrl = "",
            instagramUser = "Instagram",
            twitterUser = "Twitter",
            userLink = "Link"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InterestPreview() {
    val list = listOf("Art", "Photographer", "Minimal", "Abstract", "Monochrome",
        "Vivid", "Sun")

    InterestItem(tags = list)
}