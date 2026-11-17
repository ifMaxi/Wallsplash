@file:OptIn(ExperimentalCoilApi::class)

package com.maxidev.wallsplash.presentation.detail.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.R
import com.maxidev.wallsplash.domain.model.User

@Composable
fun UserScreen(
    viewModel: UserViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoadStateScreen(
        uiState = state
    )
}

@Composable
private fun LoadStateScreen(
    uiState: UserUiState
) {
    when {
        uiState.isLoading -> {}
        uiState.isError != null -> {}
        else -> {
            ScreenContent(
                user = uiState.user ?: return
            )
        }
    }
}

@Composable
private fun ScreenContent(
    user: User
) {
    val lazyState = rememberLazyStaggeredGridState()

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
        }
    }
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
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
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
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = bio
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(text = location)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Socials"
                    )
                    Box {
                        IconButton(onClick = { expanded = !expanded} ) {
                            Icon(
                                painter = painterResource(R.drawable.more_vert),
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(text = "X (Twitter)") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.x_icon),
                                        contentDescription = "X (Twitter)"
                                    )
                                },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Instagram") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.instagram_icon),
                                        contentDescription = "Instagram"
                                    )
                                },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Portfolio") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.work),
                                        contentDescription = "Portfolio"
                                    )
                                },
                                onClick = {}
                            )
                            DropdownMenuItem(
                                text = { Text(text = "Unsplash profile") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.link),
                                        contentDescription = "Unsplash profile"
                                    )
                                },
                                onClick = {}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
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