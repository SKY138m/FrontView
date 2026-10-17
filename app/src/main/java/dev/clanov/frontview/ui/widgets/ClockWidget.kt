package dev.clanov.frontview.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.clanov.frontview.ui.AnalogClock
import dev.clanov.frontview.ui.HGap
import dev.clanov.frontview.ui.Palette
import dev.clanov.frontview.ui.SlotSize
import dev.clanov.frontview.ui.T
import dev.clanov.frontview.ui.TA
import dev.clanov.frontview.ui.Tile
import dev.clanov.frontview.ui.VGap
import dev.clanov.frontview.ui.digitalTime
import dev.clanov.frontview.ui.dx
import dev.clanov.frontview.ui.monthDay
import dev.clanov.frontview.ui.panel
import dev.clanov.frontview.ui.rememberNow
import dev.clanov.frontview.ui.weekday
import dev.clanov.frontview.ui.worldClocks

@Composable
fun ClockWidget(size: SlotSize) {
    val now by rememberNow()
    Tile(size, Palette.Tile) {
        when (size) {
            SlotSize.M -> Column(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalArrangement = Arrangement.Center
            ) {
                TA(digitalTime(now), 68, weight = 300, tabular = true)
                T("${now.monthDay()} ${now.weekday()}", 22, Palette.Muted)
            }
            SlotSize.L -> Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnalogClock(240, now, Palette.Tile)
                VGap(10)
                T("${now.monthDay()} ${now.weekday()}", 22, Palette.Muted)
            }
            SlotSize.XL -> Row(
                Modifier.fillMaxSize().padding(horizontal = 28.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnalogClock(288, now, Palette.Tile)
                HGap(36)
                Column(Modifier.fillMaxWidth()) {
                    TA(digitalTime(now), 72, weight = 300, tabular = true)
                    T("${now.monthDay()} ${now.weekday()}", 24, Palette.Muted)
                    VGap(18)
                    worldClocks(now).forEach { w ->
                        Row(
                            Modifier.fillMaxWidth().padding(bottom = 8.dx).panel(Palette.Background, 14)
                                .padding(horizontal = 18.dx, vertical = 10.dx),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                T(w.city, 22, Palette.Ink, 600)
                                T(w.note, 16, Palette.Muted)
                            }
                            T(w.time, 34, Palette.Ink, 300, tabular = true)
                        }
                    }
                }
            }
        }
    }
}
