package dev.clanov.frontview.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import kotlin.math.cos
import kotlin.math.sin

enum class GlyphKind {
    Close, Pause, Prev, Next, Sun, Cloud, Moon, Mic, Mail, Swap, Plus, Pin, External, Check,
    Calendar, Cart, Car, Home, List, Dots, Volume, Lyrics, AirPlay
}

/** Simple line icons drawn on a 24 x 24 grid. */
@Composable
fun Glyph(kind: GlyphKind, size: Int, color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier.size(size.dx)) {
        val unit = this.size.minDimension / 24f
        scale(unit, unit, pivot = Offset.Zero) {
            drawGlyph(kind, color)
        }
    }
}

private fun DrawScope.drawGlyph(kind: GlyphKind, color: Color) {
    val line = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    val thin = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    when (kind) {
        GlyphKind.Close -> {
            drawLine(color, Offset(6f, 6f), Offset(18f, 18f), strokeWidth = 2.4f, cap = StrokeCap.Round)
            drawLine(color, Offset(18f, 6f), Offset(6f, 18f), strokeWidth = 2.4f, cap = StrokeCap.Round)
        }
        GlyphKind.Pause -> {
            drawRoundRect(color, Offset(6f, 4.5f), Size(4f, 15f), CornerRadius(1.2f, 1.2f))
            drawRoundRect(color, Offset(14f, 4.5f), Size(4f, 15f), CornerRadius(1.2f, 1.2f))
        }
        GlyphKind.Prev -> {
            val p = Path().apply {
                moveTo(11f, 6f); lineTo(11f, 18f); lineTo(2.5f, 12f); close()
                moveTo(21.5f, 6f); lineTo(21.5f, 18f); lineTo(13f, 12f); close()
            }
            drawPath(p, color)
        }
        GlyphKind.Next -> {
            val p = Path().apply {
                moveTo(13f, 6f); lineTo(13f, 18f); lineTo(21.5f, 12f); close()
                moveTo(2.5f, 6f); lineTo(2.5f, 18f); lineTo(11f, 12f); close()
            }
            drawPath(p, color)
        }
        GlyphKind.Sun -> {
            drawCircle(color, radius = 4.4f, center = Offset(12f, 12f))
            for (i in 0 until 8) {
                val a = Math.toRadians(i * 45.0)
                val c = cos(a).toFloat()
                val s = sin(a).toFloat()
                drawLine(
                    color,
                    Offset(12f + 7.4f * c, 12f + 7.4f * s),
                    Offset(12f + 9.6f * c, 12f + 9.6f * s),
                    strokeWidth = 1.8f,
                    cap = StrokeCap.Round
                )
            }
        }
        GlyphKind.Cloud -> {
            drawCircle(color, radius = 4f, center = Offset(9f, 13.5f))
            drawCircle(color, radius = 5f, center = Offset(14f, 11.5f))
            drawCircle(color, radius = 3.2f, center = Offset(17.5f, 14.8f))
            drawRect(color, topLeft = Offset(7f, 14f), size = Size(10.5f, 4f))
        }
        GlyphKind.Moon -> {
            val outer = Path().apply { addOval(Rect(Offset(12f, 12f), 8f)) }
            val cut = Path().apply { addOval(Rect(Offset(16.2f, 8.6f), 7.4f)) }
            drawPath(Path.combine(PathOperation.Difference, outer, cut), color)
        }
        GlyphKind.Mic -> {
            drawRoundRect(color, Offset(9f, 3f), Size(6f, 11f), CornerRadius(3f, 3f), style = line)
            val arc = Path().apply {
                moveTo(5f, 11f)
                arcTo(Rect(5f, 4f, 19f, 18f), 180f, -180f, false)
            }
            drawPath(arc, color, style = line)
            drawLine(color, Offset(12f, 18f), Offset(12f, 21f), strokeWidth = 2.2f, cap = StrokeCap.Round)
        }
        GlyphKind.Mail -> {
            drawRoundRect(color, Offset(3f, 5f), Size(18f, 14f), CornerRadius(2.5f, 2.5f), style = line)
            val p = Path().apply {
                moveTo(3.5f, 7f); lineTo(12f, 13f); lineTo(20.5f, 7f)
            }
            drawPath(p, color, style = line)
        }
        GlyphKind.Swap -> {
            val p = Path().apply {
                moveTo(7f, 7f); lineTo(18f, 7f); lineTo(15f, 4f)
                moveTo(17f, 17f); lineTo(6f, 17f); lineTo(9f, 20f)
            }
            drawPath(p, color, style = line)
        }
        GlyphKind.Plus -> {
            drawLine(color, Offset(12f, 5f), Offset(12f, 19f), strokeWidth = 2.6f, cap = StrokeCap.Round)
            drawLine(color, Offset(5f, 12f), Offset(19f, 12f), strokeWidth = 2.6f, cap = StrokeCap.Round)
        }
        GlyphKind.Pin -> {
            val p = Path().apply {
                moveTo(5f, 9.5f)
                arcTo(Rect(5f, 2.5f, 19f, 16.5f), 180f, 180f, false)
                lineTo(12f, 21f)
                close()
            }
            drawPath(p, color, style = line)
            drawCircle(color, radius = 2.5f, center = Offset(12f, 9.5f), style = line)
        }
        GlyphKind.External -> {
            val p = Path().apply {
                moveTo(14f, 4f); lineTo(20f, 4f); lineTo(20f, 10f)
                moveTo(20f, 4f); lineTo(11f, 13f)
                moveTo(18f, 14f); lineTo(18f, 19f); lineTo(5f, 19f); lineTo(5f, 6f); lineTo(10f, 6f)
            }
            drawPath(p, color, style = line)
        }
        GlyphKind.Check -> {
            val p = Path().apply {
                moveTo(5f, 12.5f); lineTo(9.5f, 17f); lineTo(19f, 7.5f)
            }
            drawPath(p, color, style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
        GlyphKind.Calendar -> {
            drawRoundRect(color, Offset(3f, 5f), Size(18f, 16f), CornerRadius(3f, 3f), style = thin)
            drawLine(color, Offset(3f, 10f), Offset(21f, 10f), strokeWidth = 1.8f, cap = StrokeCap.Round)
            drawLine(color, Offset(8f, 3f), Offset(8f, 7f), strokeWidth = 1.8f, cap = StrokeCap.Round)
            drawLine(color, Offset(16f, 3f), Offset(16f, 7f), strokeWidth = 1.8f, cap = StrokeCap.Round)
        }
        GlyphKind.Cart -> {
            val p = Path().apply {
                moveTo(3f, 4f); lineTo(5.5f, 4f); lineTo(7.5f, 15f); lineTo(17.5f, 15f); lineTo(19.5f, 7f); lineTo(7f, 7f)
            }
            drawPath(p, color, style = thin)
            drawCircle(color, radius = 1.5f, center = Offset(9f, 19.5f), style = thin)
            drawCircle(color, radius = 1.5f, center = Offset(17f, 19.5f), style = thin)
        }
        GlyphKind.Car -> {
            val p = Path().apply {
                moveTo(4f, 16f); lineTo(4f, 12f); lineTo(6f, 7f); lineTo(18f, 7f); lineTo(20f, 12f); lineTo(20f, 16f); close()
                moveTo(4f, 12f); lineTo(20f, 12f)
            }
            drawPath(p, color, style = thin)
            drawCircle(color, radius = 1.8f, center = Offset(7.5f, 18.5f), style = thin)
            drawCircle(color, radius = 1.8f, center = Offset(16.5f, 18.5f), style = thin)
        }
        GlyphKind.Home -> {
            val p = Path().apply {
                moveTo(4f, 11f); lineTo(12f, 4f); lineTo(20f, 11f); lineTo(20f, 20f); lineTo(4f, 20f); close()
                moveTo(10f, 20f); lineTo(10f, 14f); lineTo(14f, 14f); lineTo(14f, 20f)
            }
            drawPath(p, color, style = thin)
        }
        GlyphKind.List -> {
            for (y in listOf(6f, 12f, 18f)) {
                drawLine(color, Offset(8f, y), Offset(20f, y), strokeWidth = 2.2f, cap = StrokeCap.Round)
                drawCircle(color, radius = 1.2f, center = Offset(4f, y))
            }
        }
        GlyphKind.Dots -> {
            drawCircle(color, radius = 2f, center = Offset(5f, 12f))
            drawCircle(color, radius = 2f, center = Offset(12f, 12f))
            drawCircle(color, radius = 2f, center = Offset(19f, 12f))
        }
        GlyphKind.Volume -> {
            val p = Path().apply {
                moveTo(4f, 9.5f); lineTo(4f, 14.5f); lineTo(8f, 14.5f); lineTo(13f, 18.5f); lineTo(13f, 5.5f); lineTo(8f, 9.5f); close()
            }
            drawPath(p, color)
            val wave = Path().apply {
                moveTo(16.5f, 9f)
                arcTo(Rect(12.5f, 8f, 20.5f, 16f), -50f, 100f, false)
            }
            drawPath(wave, color, style = thin)
        }
        GlyphKind.Lyrics -> {
            val p = Path().apply {
                moveTo(4f, 5f); lineTo(20f, 5f); lineTo(20f, 16f); lineTo(9f, 16f); lineTo(4f, 20f); close()
            }
            drawPath(p, color, style = line)
            drawLine(color, Offset(8f, 9.5f), Offset(16f, 9.5f), strokeWidth = 2f, cap = StrokeCap.Round)
            drawLine(color, Offset(8f, 12.5f), Offset(13f, 12.5f), strokeWidth = 2f, cap = StrokeCap.Round)
        }
        GlyphKind.AirPlay -> {
            drawRoundRect(color, Offset(2f, 4f), Size(20f, 13f), CornerRadius(2f, 2f), style = thin)
            val p = Path().apply {
                moveTo(12f, 15f); lineTo(17f, 21f); lineTo(7f, 21f); close()
            }
            drawPath(p, color)
        }
    }
}
