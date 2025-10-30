package com.maxidev.wallsplash.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxidev.wallsplash.R

@Composable
fun CustomNetworkErrorItem(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit = {}
) {
    val isSystemTheme = isSystemInDarkTheme()
    val tintColor = MaterialTheme.colorScheme.outline
    val changeTint = if (isSystemTheme) tintColor else tintColor
    val modPadding = 8.dp

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.shrug_image),
            contentDescription = "Something went wrong.",
            colorFilter = ColorFilter.tint(changeTint),
            modifier = Modifier
                .padding(modPadding)
                .size(120.dp)
        )
        Spacer(modifier = Modifier.padding(modPadding))

        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.padding(modPadding))

        if (onRetry != {}) {
            Button(
                onClick = onRetry,
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(modPadding)
            ) {
                Text(text = "Retry")
            }
        }
    }
}

@Composable
fun CustomNetworkErrorForPagingItem(
    modifier: Modifier = Modifier,
    message: String
) {
    val isSystemTheme = isSystemInDarkTheme()
    val tintColor = MaterialTheme.colorScheme.outline
    val changeTint = if (isSystemTheme) tintColor else tintColor
    val modPadding = 8.dp

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.shrug_image),
            contentDescription = "Something went wrong.",
            colorFilter = ColorFilter.tint(changeTint),
            modifier = Modifier
                .padding(modPadding)
                .size(120.dp)
        )
        Spacer(modifier = Modifier.padding(modPadding))

        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CustomNetworkLoadingItem() {
    val isSystemTheme = isSystemInDarkTheme()
    val tintColor = MaterialTheme.colorScheme.outline
    val changeTint = if (isSystemTheme) tintColor else tintColor
    val modPadding = 8.dp

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.hour_glass_image),
            contentDescription = "Loading",
            colorFilter = ColorFilter.tint(changeTint),
            modifier = Modifier
                .padding(modPadding)
                .size(120.dp)
        )

        Spacer(modifier = Modifier.padding(modPadding))

        LinearProgressIndicator()
    }
}

/* Preview composables */

@Preview(showBackground = true)
@Composable
private fun CustomNetworkErrorItemPreview() {
    CustomNetworkErrorItem(
        message = "Something went wrong.",
        onRetry = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomNetworkLoadingItemPreview() {
    CustomNetworkLoadingItem()
}