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
fun CalendarWidget(size: SlotSize) {
    val now by rememberNow()
    Tile(size, Palette.Tile) {
        when (size) {
            SlotSize.M -> Row(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    T(now.weekday().take(1), 18, Palette.Red, 700)
                    T(now.dayOfMonth.toString(), 56, Palette.Ink, 300)
                }
                HGap(20)
                val e = SampleData.events.first()
                Column {
                    T(e.title, 24, Palette.Ink, 600)
                    T(e.time, 18, Palette.Muted)
                }
            }
            SlotSize.L -> Column(Modifier.fillMaxSize().padding(24.dx)) {
                T("${now.monthDay()} ${now.weekday()}", 22, Palette.Red, 700)
                VGap(12)
                SampleData.events.forEach { EventLine(it) }
            }
            SlotSize.XL -> Row(Modifier.fillMaxSize().padding(28.dx)) {
                Column(Modifier.width(220.dx)) {
                    T(now.weekday(), 24, Palette.Red, 700)
                    T(now.dayOfMonth.toString(), 140, Palette.Ink, 200)
                    T("${now.monthValue}월", 24, Palette.Muted)
                }
                HGap(24)
                Column(Modifier.fillMaxWidth()) {
                    SampleData.events.forEach { EventLine(it) }
                }
            }
        }
    }
}

@Composable
private fun EventLine(e: EventRow) {
    Row(Modifier.fillMaxWidth().padding(bottom = 10.dx), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(5.dx).height(46.dx).clip(CircleShape).background(e.color))
        HGap(12)
        Column {
            T(e.title, 22, Palette.Ink, 600)
            T(e.time, 16, Palette.Muted)
        }
    }
}
