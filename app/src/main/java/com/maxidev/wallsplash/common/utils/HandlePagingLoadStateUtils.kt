package com.maxidev.wallsplash.common.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import retrofit2.HttpException
import java.io.IOException

/**
 * Extension that handles pagination loading states.
 *
 * @param loadState A CombinedLoadStates object which represents the current loading state.
 * @param itemCount The number of items which can be accessed.
 */
fun LazyStaggeredGridScope.handlePagingLoadState(
    loadState: CombinedLoadStates,
    itemCount: Int
) {
    val centerAlign = Alignment.Center
    val sharedModifier = Modifier
        .fillMaxSize()
        .padding(16.dp)

    loadState.let { states ->
        when {
            states.refresh is LoadState.NotLoading && itemCount < 1 -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(
                        modifier = sharedModifier,
                        contentAlignment = centerAlign
                    ) {
                        Text(
                            text = "No data available.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            states.refresh is LoadState.Error -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(
                        modifier = sharedModifier,
                        contentAlignment = centerAlign
                    ) {
                        val loadRefresh = states.refresh as LoadState.Error

                        Text(
                            text = when (loadRefresh.error) {
                                is HttpException -> "Something went wrong."
                                is IOException -> "No internet connection."
                                else -> "Unknown error."
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            states.append is LoadState.Loading -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(
                        modifier = sharedModifier,
                        contentAlignment = centerAlign
                    ) {
                        LinearProgressIndicator()
                    }
                }
            }
            states.append is LoadState.Error -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(
                        modifier = sharedModifier,
                        contentAlignment = centerAlign
                    ) {
                        Text(
                            text = "Something went wrong.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}