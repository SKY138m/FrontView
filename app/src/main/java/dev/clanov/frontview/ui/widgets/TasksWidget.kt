package dev.clanov.frontview.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.*
import dev.clanov.frontview.ui.sample.*

@Composable
fun TasksWidget(size: SlotSize) {
    Tile(size, Palette.Tile) {
        when (size) {
            SlotSize.M -> Row(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    T("할 일", 28, Palette.Blue, 700)
                    T("오늘 ${SampleData.tasks.count { !it.done }}개 남음", 20, Palette.Muted)
                }
                T("${SampleData.tasks.count { !it.done }}", 64, Palette.Ink, 300)
            }
            SlotSize.L -> Column(Modifier.fillMaxSize().padding(24.dx)) {
                T("할 일", 26, Palette.Blue, 700)
                VGap(10)
                SampleData.tasks.forEach { TaskLine(it) }
            }
            SlotSize.XL -> Row(Modifier.fillMaxSize().padding(28.dx)) {
                Column(Modifier.width(300.dx)) {
                    T("할 일", 28, Palette.Blue, 700)
                    VGap(10)
                    SampleData.taskCounts.chunked(2).forEach { pair ->
                        Row(Modifier.padding(bottom = 10.dx)) {
                            pair.forEach { c ->
                                Row(
                                    Modifier.width(142.dx).height(84.dx).panel(c.color, 18)
                                        .padding(14.dx),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    T(c.name, 18, Color.White, 600)
                                    T("${c.count}", 32, Color.White, 700)
                                }
                                HGap(8)
                            }
                        }
                    }
                }
                HGap(20)
                Column(Modifier.fillMaxWidth()) {
                    SampleData.tasks.forEach { TaskLine(it) }
                }
            }
        }
    }
}

@Composable
private fun TaskLine(t: TaskRow) {
    Row(Modifier.fillMaxWidth().padding(bottom = 10.dx), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(26.dx).clip(CircleShape)
                .background(if (t.done) Palette.Blue else Palette.Divider),
            contentAlignment = Alignment.Center
        ) {
            if (t.done) Glyph(GlyphKind.Check, 16, Color.White)
        }
        HGap(12)
        T(t.title, 22, if (t.done) Palette.Faint else Palette.Ink, 500, strike = t.done)
    }
}
