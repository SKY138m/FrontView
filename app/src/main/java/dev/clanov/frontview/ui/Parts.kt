package dev.clanov.frontview.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

/** Widget footprint on the 4 x 7 grid (one cell is 174 x 160, gap 16). */
enum class SlotSize { M, L, XL }

val SlotSize.pxW: Int get() = if (this == SlotSize.XL) 744 else 364
val SlotSize.pxH: Int get() = if (this == SlotSize.M) 160 else 336

@Composable
private fun textStyle(
    size: Int,
    color: Color,
    weight: Int,
    spacing: Float,
    align: TextAlign?,
    lineHeight: Int?,
    tabular: Boolean,
    strike: Boolean
): TextStyle = TextStyle(
    color = color,
    fontSize = size.sx,
    fontWeight = FontWeight(weight),
    letterSpacing = spacing.sx,
    lineHeight = if (lineHeight != null) lineHeight.sx else TextUnit.Unspecified,
    textAlign = align ?: TextAlign.Unspecified,
    textDecoration = if (strike) TextDecoration.LineThrough else TextDecoration.None,
    fontFeatureSettings = if (tabular) "tnum" else null
)

@Composable
fun T(
    text: String,
    size: Int,
    color: Color = Palette.Ink,
    weight: Int = 400,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    spacing: Float = 0f,
    align: TextAlign? = null,
    lineHeight: Int? = null,
    tabular: Boolean = false,
    strike: Boolean = false
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle(size, color, weight, spacing, align, lineHeight, tabular, strike),
        overflow = TextOverflow.Ellipsis,
        maxLines = maxLines
    )
}

@Composable
fun TA(
    text: AnnotatedString,
    size: Int,
    color: Color = Palette.Ink,
    weight: Int = 400,
    modifier: Modifier = Modifier,
    spacing: Float = 0f,
    tabular: Boolean = false
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = textStyle(size, color, weight, spacing, null, null, tabular, false),
        maxLines = 1
    )
}

@Composable
fun Tile(size: SlotSize, background: Brush, content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier
            .size(size.pxW.dx, size.pxH.dx)
            .clip(RoundedCornerShape(24.dx))
            .background(background),
        content = content
    )
}

@Composable
fun Tile(size: SlotSize, color: Color, content: @Composable BoxScope.() -> Unit) {
    Tile(size, SolidColor(color), content)
}

@Composable
fun VGap(h: Int) {
    Spacer(Modifier.height(h.dx))
}

@Composable
fun HGap(w: Int) {
    Spacer(Modifier.width(w.dx))
}

@Composable
fun Dot(size: Int, color: Color) {
    Box(Modifier.size(size.dx).clip(CircleShape).background(color))
}

@Composable
fun Modifier.panel(color: Color, radius: Int = 18): Modifier =
    this.clip(RoundedCornerShape(radius.dx)).background(color)

@Composable
fun Modifier.sized(w: Int, h: Int): Modifier = this.size(w.dx, h.dx)

@Composable
fun Segments(filled: Int, total: Int, on: Color, off: Color, height: Int, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dx)) {
        for (i in 0 until total) {
            Box(
                Modifier
                    .weight(1f)
                    .height(height.dx)
                    .clip(RoundedCornerShape((height / 2f).dx))
                    .background(if (i < filled) on else off)
            )
        }
    }
}

@Composable
fun Bar(fraction: Float, track: Color, fill: Color, height: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height.dx)
            .clip(RoundedCornerShape((height / 2f).dx))
            .background(track)
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction.coerceIn(0f, 1f))
                .background(fill)
        )
    }
}

@Composable
fun CountBadge(text: String, bg: Color, fg: Color, size: Int = 28) {
    Box(
        Modifier
            .defaultMinSize(minWidth = size.dx, minHeight = size.dx)
            .clip(RoundedCornerShape((size / 2f).dx))
            .background(bg)
            .padding(horizontal = (size * 0.28f).dx),
        contentAlignment = Alignment.Center
    ) {
        T(text, (size * 0.54f).toInt(), fg, 700)
    }
}
