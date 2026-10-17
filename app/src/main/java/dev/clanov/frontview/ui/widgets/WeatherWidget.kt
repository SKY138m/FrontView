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
fun WeatherWidget(size: SlotSize) {
    Tile(size, Palette.Weather) {
        when (size) {
            SlotSize.M -> Row(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    T(SampleData.location, 20, Palette.OnDark85, 600)
                    T(SampleData.weatherSummary.substringBefore(" ·"), 20, Palette.OnDark70)
                }
                T(SampleData.temperature, 72, Color.White, 200)
            }
            SlotSize.L -> Column(Modifier.fillMaxSize().padding(24.dx)) {
                T(SampleData.location, 22, Palette.OnDark85, 600)
                VGap(4)
                T(SampleData.temperature, 112, Color.White, 200)
                T(SampleData.weatherSummary, 20, Palette.OnDark70)
                VGap(18)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SampleData.hours.take(4).forEach { h -> HourCell(h) }
                }
            }
            SlotSize.XL -> Row(Modifier.fillMaxSize().padding(28.dx), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.width(260.dx)) {
                    T(SampleData.location, 24, Palette.OnDark85, 600)
                    T(SampleData.temperature, 128, Color.White, 200)
                    T(SampleData.weatherSummary, 20, Palette.OnDark70)
                }
                HGap(24)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SampleData.hours.forEach { h -> HourCell(h) }
                }
            }
        }
    }
}

@Composable
private fun HourCell(h: HourForecast) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        T(h.label, 18, Palette.OnDark70)
        VGap(8)
        SkyGlyph(h.sky, 32, Palette.Yellow.takeIf { h.sky == Sky.Sun } ?: Color.White)
        VGap(8)
        T("${h.temp}°", 24, Color.White, 600)
    }
}
