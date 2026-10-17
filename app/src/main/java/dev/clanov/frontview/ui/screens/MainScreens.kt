package dev.clanov.frontview.ui.screens

import androidx.compose.foundation.background
import dev.clanov.frontview.data.AppActions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import dev.clanov.frontview.ui.*
import dev.clanov.frontview.ui.sample.*
import dev.clanov.frontview.ui.widgets.*

/** Shared shell of every fullscreen view: close button on the left, live time on the right. */
@Composable
fun FullFrame(
    background: Brush,
    onClose: () -> Unit,
    dark: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val now by rememberNow()
    val fg = if (dark) Color.White else Palette.Ink
    Column(Modifier.fillMaxSize().background(background).padding(36.dx)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(52.dx).clip(CircleShape)
                    .background(if (dark) Palette.GlassStrong else Palette.Tile)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) { Glyph(GlyphKind.Close, 28, fg) }
            T(now.hhmm(), 24, fg, 500, tabular = true)
        }
        VGap(20)
        content()
    }
}

@Composable
fun FullFrame(
    background: Color,
    onClose: () -> Unit,
    dark: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) = FullFrame(SolidColor(background), onClose, dark, content)

@Composable
fun ClockScreen(onClose: () -> Unit) {
    val now by rememberNow()
    FullFrame(Palette.Background, onClose) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            AnalogClock(600, now, Palette.Background)
            VGap(14)
            T("${now.monthDay()} ${now.weekday()}", 30, Palette.Ink, 700)
            T("서울 강서구 · 한국 표준시", 20, Palette.Muted)
        }
        VGap(24)
        T("세계 시계", 22, Palette.Ink, 700)
        VGap(8)
        worldClocks(now).forEach { w ->
            Row(
                Modifier.fillMaxWidth().padding(bottom = 8.dx).height(84.dx).panel(Palette.Tile, 20)
                    .padding(horizontal = 24.dx),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    T(w.city, 24, Palette.Ink, 600)
                    T(w.note, 16, Palette.Muted)
                }
                T(w.time, 46, Palette.Ink, 300, tabular = true)
            }
        }
    }
}

@Composable
fun WeatherScreen(onClose: () -> Unit) {
    FullFrame(Palette.Weather, onClose, dark = true) {
        T(SampleData.location, 32, Color.White, 600)
        T(SampleData.temperature, 156, Color.White, 200)
        T(SampleData.weatherSummary, 24, Palette.OnDark85)
        VGap(20)
        Row(
            Modifier.fillMaxWidth().panel(Palette.Glass, 24).padding(22.dx),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SampleData.hours.forEach { h ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    T(h.label, 18, Palette.OnDark70)
                    VGap(8)
                    SkyGlyph(h.sky, 36, if (h.sky == Sky.Sun) Palette.Yellow else Color.White)
                    VGap(8)
                    T("${h.temp}°", 26, Color.White, 600)
                }
            }
        }
        VGap(16)
        Column(Modifier.fillMaxWidth().panel(Palette.Glass, 24).padding(horizontal = 22.dx, vertical = 14.dx)) {
            SampleData.days.forEach { d ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 6.dx),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    T(d.name, 24, Color.White, 600, modifier = Modifier.width(90.dx))
                    SkyGlyph(d.sky, 30, if (d.sky == Sky.Sun) Palette.Yellow else Color.White)
                    T("${d.low}°", 24, Palette.OnDark70, modifier = Modifier.width(70.dx))
                    T("${d.high}°", 24, Color.White, 600, modifier = Modifier.width(70.dx))
                }
            }
        }
        VGap(16)
        SampleData.weatherDetails.chunked(2).forEach { pair ->
            Row(Modifier.fillMaxWidth().padding(bottom = 12.dx), horizontalArrangement = Arrangement.spacedBy(12.dx)) {
                pair.forEach { d ->
                    Column(Modifier.weight(1f).panel(Palette.Glass, 20).padding(18.dx)) {
                        T(d.label, 18, Palette.OnDark70)
                        T(d.value + if (d.unit.isNotEmpty()) " ${d.unit}" else "", 32, Color.White, 600)
                    }
                }
            }
        }
    }
}

@Composable
fun TasksScreen(onClose: () -> Unit) {
    FullFrame(Palette.Grouped, onClose) {
        T("할 일", 52, Palette.Ink, 700)
        VGap(12)
        SampleData.taskCounts.chunked(2).forEach { pair ->
            Row(Modifier.fillMaxWidth().padding(bottom = 12.dx), horizontalArrangement = Arrangement.spacedBy(12.dx)) {
                pair.forEach { c ->
                    Row(
                        Modifier.weight(1f).height(136.dx).panel(c.color, 24).padding(22.dx),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        T(c.name, 26, Color.White, 600)
                        T("${c.count}", 44, Color.White, 700)
                    }
                }
            }
        }
        Column(Modifier.fillMaxWidth().panel(Color.White, 20).padding(horizontal = 22.dx, vertical = 8.dx)) {
            SampleData.taskLists.forEach { l ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 12.dx),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    T(l.name, 24, Palette.Ink, 500)
                    T("${l.count}", 24, Palette.Faint)
                }
            }
            T("목록 추가", 22, Palette.Blue, 600, modifier = Modifier.padding(vertical = 12.dx))
        }
        VGap(20)
        T("오늘", 28, Palette.Ink, 700)
        VGap(8)
        Column(Modifier.fillMaxWidth().panel(Color.White, 20).padding(horizontal = 22.dx, vertical = 10.dx)) {
            SampleData.tasks.forEach { t ->
                Row(Modifier.fillMaxWidth().clickable { AppActions.completeTask(t) }.padding(vertical = 12.dx), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(32.dx).clip(CircleShape)
                            .background(if (t.done) Palette.Blue else Palette.Divider),
                        contentAlignment = Alignment.Center
                    ) { if (t.done) Glyph(GlyphKind.Check, 20, Color.White) }
                    HGap(16)
                    T(t.title, 26, if (t.done) Palette.Faint else Palette.Ink, 500, strike = t.done)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Box(
            Modifier.fillMaxWidth().height(64.dx).panel(Palette.Blue, 32).clickable { AppActions.addTask() },
            contentAlignment = Alignment.Center
        ) { T("새 할 일", 24, Color.White, 600) }
    }
}

@Composable
fun CalendarScreen(onClose: () -> Unit) {
    val now by rememberNow()
    val first = now.withDayOfMonth(1)
    val lead = first.dayOfWeek.value % 7
    val days = now.toLocalDate().lengthOfMonth()
    FullFrame(Palette.Background, onClose) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            T("${now.year}년 ${now.monthValue}월", 44, Palette.Ink, 700)
            Box(Modifier.panel(Palette.Tile, 20).padding(horizontal = 20.dx, vertical = 8.dx)) { T("오늘", 20, Palette.Red, 600) }
        }
        VGap(10)
        Row(Modifier.fillMaxWidth()) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) { T(it, 18, Palette.Muted, 600) }
            }
        }
        val cells = lead + days
        for (r in 0 until (cells + 6) / 7) {
            Row(Modifier.fillMaxWidth().height(92.dx)) {
                for (c in 0 until 7) {
                    val d = r * 7 + c - lead + 1
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        VGap(6)
                        if (d in 1..days) {
                            val today = d == now.dayOfMonth
                            Box(
                                Modifier.size(44.dx).clip(CircleShape).background(if (today) Palette.Red else Color.Transparent),
                                contentAlignment = Alignment.Center
                            ) { T("$d", 24, if (today) Color.White else Palette.Ink, if (today) 700 else 400) }
                            VGap(4)
                            Row(horizontalArrangement = Arrangement.spacedBy(3.dx)) {
                                SampleData.eventDots[d]?.forEach { Dot(8, it) }
                            }
                        }
                    }
                }
            }
        }
        VGap(8)
        T("${now.monthDay()} ${now.weekday()}", 26, Palette.Ink, 700)
        VGap(8)
        SampleData.events.forEach { e ->
            Row(Modifier.fillMaxWidth().padding(bottom = 10.dx), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.width(6.dx).height(56.dx).clip(CircleShape).background(e.color))
                HGap(16)
                Column {
                    T(e.title, 24, Palette.Ink, 600)
                    T(e.time, 18, Palette.Muted)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Box(Modifier.fillMaxWidth().height(64.dx).panel(Palette.Lavender, 32), contentAlignment = Alignment.Center) {
            T("새 일정", 24, Color.White, 600)
        }
    }
}

@Composable
fun MusicScreen(onClose: () -> Unit) {
    FullFrame(Palette.MusicDark, onClose, dark = true) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 36.dx), horizontalAlignment = Alignment.CenterHorizontally) {
            VGap(20)
            Art(600)
            VGap(30)
            T(MusicSample.TITLE, 44, Color.White, 700)
            T(MusicSample.ARTIST, 28, Palette.OnDark70)
            VGap(24)
            Bar(0.38f, Palette.Glass, Color.White, 8)
            VGap(8)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                T("1:24", 18, Palette.OnDark60)
                T("-2:17", 18, Palette.OnDark60)
            }
            VGap(24)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Glyph(GlyphKind.Prev, 56, Color.White, Modifier.clickable { AppActions.music("previous") })
                Glyph(if (MusicSample.isPlaying) GlyphKind.Pause else GlyphKind.Next, 72, Color.White, Modifier.clickable { AppActions.music("toggle") })
                Glyph(GlyphKind.Next, 56, Color.White, Modifier.clickable { AppActions.music("next") })
            }
            VGap(28)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Glyph(GlyphKind.Volume, 28, Palette.OnDark70)
                HGap(14)
                Bar(0.62f, Palette.Glass, Palette.OnDark90, 6, Modifier.weight(1f))
            }
            VGap(24)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Glyph(GlyphKind.Lyrics, 32, Palette.OnDark70)
                Glyph(GlyphKind.AirPlay, 32, Palette.OnDark70)
                Glyph(GlyphKind.List, 32, Palette.OnDark70)
            }
        }
    }
}
