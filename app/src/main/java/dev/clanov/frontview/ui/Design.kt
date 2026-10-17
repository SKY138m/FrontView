package dev.clanov.frontview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Design reference: 800 x 1280 px portrait. All sizes are written in these units. */
const val DESIGN_WIDTH = 800
const val DESIGN_HEIGHT = 1280

val LocalDesignScale = compositionLocalOf { 1f }

val Int.dx: Dp
    @Composable @ReadOnlyComposable get() = (this * LocalDesignScale.current).dp

val Float.dx: Dp
    @Composable @ReadOnlyComposable get() = (this * LocalDesignScale.current).dp

val Int.sx: TextUnit
    @Composable @ReadOnlyComposable get() = (this * LocalDesignScale.current).sp

val Float.sx: TextUnit
    @Composable @ReadOnlyComposable get() = (this * LocalDesignScale.current).sp

/** Fits the 800 x 1280 design into the space left by the system bars. */
@Composable
fun DesignCanvas(content: @Composable BoxScope.() -> Unit) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(Palette.Background)
            .systemBarsPadding()
    ) {
        val scale = minOf(maxWidth.value / DESIGN_WIDTH, maxHeight.value / DESIGN_HEIGHT)
        CompositionLocalProvider(LocalDesignScale provides scale) {
            Box(
                Modifier
                    .size((DESIGN_WIDTH * scale).dp, (DESIGN_HEIGHT * scale).dp)
                    .align(Alignment.Center),
                content = content
            )
        }
    }
}
