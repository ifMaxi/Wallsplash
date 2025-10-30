package com.maxidev.wallsplash.common.utils

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.maxidev.wallsplash.common.presentation.components.CustomNetworkErrorForPagingItem
import com.maxidev.wallsplash.common.presentation.components.CustomNetworkLoadingItem
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
    loadState.let { states ->
        when {
            states.refresh is LoadState.NotLoading && itemCount < 1 -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    CustomNetworkErrorForPagingItem(message = "No data available")
                }
            }
            states.refresh is LoadState.Error -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    val loadRefresh = states.refresh as LoadState.Error

                    CustomNetworkErrorForPagingItem(
                        message = when (loadRefresh.error) {
                            is HttpException -> "Something went wrong."
                            is IOException -> "No internet connection."
                            else -> "Unknown error."
                        }
                    )
                }
            }
            states.append is LoadState.Loading -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    CustomNetworkLoadingItem()
                }
            }
            states.append is LoadState.Error -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    CustomNetworkErrorForPagingItem(message = "Something went wrong.")
                }
            }
        }
    }
}