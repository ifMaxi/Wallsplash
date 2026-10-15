package com.maxidev.wallsplash.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.sentiment_dissatisfied),
            contentDescription = message,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .padding(16.dp)
                .size(140.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
        ) {
            Text(text = "Retry")
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CustomNetworkErrorForPagingItem(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.sentiment_dissatisfied),
            contentDescription = message,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .padding(16.dp)
                .size(140.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        ElevatedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp)
        ) {
            Text(text = "Retry")
        }
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CustomNetworkLoadingItem() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.cycle),
            contentDescription = "Loading",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outline),
            modifier = Modifier
                .padding(16.dp)
                .size(140.dp)
        )
        Spacer(modifier = Modifier.padding(16.dp))
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
private fun CustomErrorPaging() {
    CustomNetworkErrorForPagingItem(
        message = "Lorem impsum",
        onRetry = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomNetworkLoadingItemPreview() {
    CustomNetworkLoadingItem()
}