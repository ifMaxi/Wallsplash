package com.maxidev.wallsplash.presentation.collections

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.maxidev.wallsplash.presentation.components.CustomAsyncImage
import com.maxidev.wallsplash.presentation.components.UserItem
import com.maxidev.wallsplash.domain.model.Collections
import com.wajahatiqbal.blurhash.BlurHashPainter

@Composable
fun CollectionsScreen() {
}

@Composable
private fun CollectionItem(
    model: Collections,
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

    Column(
        modifier = Modifier
            .padding(modPadding)
            .clickable { onClick() },
        verticalArrangement = Arrangement.spacedBy(modPadding)
    ) {
        UserItem(
            image = model.profileImageLarge,
            user = model.name,
            navigateToUserDetail = {}
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(modPadding)
                .background(color = colorWhite, shape = roundedCornerShape)
        ) {
            CustomAsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .clip(roundedCornerShape),
                model = model.urlRegular,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                blurHash = blurHolder
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
            onClick = {}
        )
    }
}