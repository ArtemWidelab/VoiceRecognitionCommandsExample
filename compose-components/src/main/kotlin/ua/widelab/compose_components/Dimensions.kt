package ua.widelab.compose_components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val innerCardPadding: Dp = 8.dp,
    val cardPadding: Dp = 8.dp,
    val screenPadding: Dp = 16.dp,
    val bigPadding: Dp = 32.dp
)

public val MaterialTheme.dimensions
    @Composable
    @ReadOnlyComposable
    get() = Dimensions()