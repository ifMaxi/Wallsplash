package com.maxidev.wallsplash.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.plusBottom(extra: Dp): PaddingValues {
    val dir = LocalLayoutDirection.current

    return PaddingValues(
        start = calculateStartPadding(dir),
        top = calculateTopPadding(),
        end = calculateEndPadding(dir),
        bottom = calculateBottomPadding() + extra
    )
}