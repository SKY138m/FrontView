package dev.clanov.frontview.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun rememberNow(): State<ZonedDateTime> =
    produceState(initialValue = ZonedDateTime.now()) {
        while (true) {
            value = ZonedDateTime.now()
            delay(1_000)
        }
    }

private val hhmmFormat = DateTimeFormatter.ofPattern("HH:mm")
private val monthDayFormat = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN)
private val weekdayFormat = DateTimeFormatter.ofPattern("EEEE", Locale.KOREAN)

fun ZonedDateTime.hhmm(): String = format(hhmmFormat)
fun ZonedDateTime.monthDay(): String = format(monthDayFormat)
fun ZonedDateTime.weekday(): String = format(weekdayFormat)

data class WorldClock(val city: String, val time: String, val note: String)

fun worldClocks(now: ZonedDateTime): List<WorldClock> {
    val zones = listOf(
        "뉴욕" to "America/New_York",
        "런던" to "Europe/London",
        "로스앤젤레스" to "America/Los_Angeles"
    )
    return zones.map { (city, id) ->
        val there = now.withZoneSameInstant(ZoneId.of(id))
        val diff = (now.offset.totalSeconds - there.offset.totalSeconds) / 3600
        val day = when {
            there.toLocalDate().isBefore(now.toLocalDate()) -> "어제"
            there.toLocalDate().isAfter(now.toLocalDate()) -> "내일"
            else -> "오늘"
        }
        val relation = if (diff >= 0) "${diff}시간 느림" else "${-diff}시간 빠름"
        WorldClock(city, there.hhmm(), "$day · $relation")
    }
}

fun digitalTime(now: ZonedDateTime): AnnotatedString = buildAnnotatedString {
    append(now.format(DateTimeFormatter.ofPattern("HH")))
    withStyle(SpanStyle(color = Palette.Lavender)) { append(":") }
    append(now.format(DateTimeFormatter.ofPattern("mm")))
}

@Composable
fun AnalogClock(size: Int, now: ZonedDateTime, hub: Color) {
    val measurer = rememberTextMeasurer()
    val hour = (now.hour % 12 + now.minute / 60f) * 30f
    val minute = (now.minute + now.second / 60f) * 6f
    val second = now.second * 6f
    Canvas(Modifier.size(size.dx)) {
        val u = this.size.minDimension / 240f
        val c = Offset(120f * u, 120f * u)
        for (n in 1..12) {
            val a = Math.toRadians(n * 30.0 - 90.0)
            val x = c.x + (96f * u * cos(a)).toFloat()
            val y = c.y + (96f * u * sin(a)).toFloat()
            val layout = measurer.measure(
                n.toString(),
                TextStyle(
                    color = Palette.Lavender,
                    fontSize = (22f * u).toSp(),
                    fontWeight = FontWeight.Medium
                )
            )
            drawText(
                layout,
                topLeft = Offset(x - layout.size.width / 2f, y - layout.size.height / 2f)
            )
        }
        rotate(hour, pivot = c) {
            drawLine(
                Palette.Ink,
                Offset(c.x, c.y + 12f * u),
                Offset(c.x, c.y - 52f * u),
                strokeWidth = 7f * u,
                cap = StrokeCap.Round
            )
        }
        rotate(minute, pivot = c) {
            drawLine(
                Palette.Ink,
                Offset(c.x, c.y + 14f * u),
                Offset(c.x, c.y - 80f * u),
                strokeWidth = 5f * u,
                cap = StrokeCap.Round
            )
        }
        rotate(second, pivot = c) {
            drawLine(
                Palette.Lavender,
                Offset(c.x, c.y + 20f * u),
                Offset(c.x, c.y - 90f * u),
                strokeWidth = 2f * u,
                cap = StrokeCap.Round
            )
        }
        drawCircle(Palette.Lavender, radius = 6.5f * u, center = c)
        drawCircle(hub, radius = 2.5f * u, center = c)
    }
}
