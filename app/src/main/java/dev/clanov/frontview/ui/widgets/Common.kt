package dev.clanov.frontview.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.Glyph
import dev.clanov.frontview.ui.GlyphKind
import dev.clanov.frontview.ui.T
import dev.clanov.frontview.ui.dx
import dev.clanov.frontview.ui.sample.Sky

@Composable
fun SkyGlyph(sky: Sky, size: Int, color: Color) {
    val kind = when (sky) {
        Sky.Sun -> GlyphKind.Sun
        Sky.Cloud -> GlyphKind.Cloud
        Sky.Moon -> GlyphKind.Moon
    }
    Glyph(kind, size, color)
}

@Composable
fun Avatar(letter: String, size: Int, bg: Color, fg: Color) {
    Box(
        Modifier.size(size.dx).clip(CircleShape).background(bg),
        contentAlignment = Alignment.Center
    ) {
        T(letter, (size * 0.42f).toInt(), fg, 700)
    }
}
