package dev.clanov.frontview.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import dev.clanov.frontview.data.AppActions
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.*
import dev.clanov.frontview.ui.sample.*

@Composable
fun MusicWidget(size: SlotSize) {
    Tile(size, Palette.MusicDark) {
        when (size) {
            SlotSize.M -> Row(
                Modifier.fillMaxSize().padding(horizontal = 20.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Art(100)
                HGap(16)
                Column(Modifier.weight(1f)) {
                    T(MusicSample.TITLE, 24, Color.White, 700)
                    T(MusicSample.ARTIST, 18, Palette.OnDark70)
                }
                Glyph(if (MusicSample.isPlaying) GlyphKind.Pause else GlyphKind.Next, 36, Color.White)
            }
            SlotSize.L -> Column(
                Modifier.fillMaxSize().padding(24.dx),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Art(180)
                VGap(14)
                T(MusicSample.TITLE, 26, Color.White, 700)
                T(MusicSample.ARTIST, 20, Palette.OnDark70)
                VGap(14)
                Bar(0.38f, Palette.Glass, Color.White, 6)
            }
            SlotSize.XL -> Row(
                Modifier.fillMaxSize().padding(28.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Art(280)
                HGap(32)
                Column(Modifier.fillMaxWidth()) {
                    T(MusicSample.TITLE, 36, Color.White, 700)
                    T(MusicSample.ARTIST, 24, Palette.OnDark70)
                    VGap(20)
                    Bar(0.38f, Palette.Glass, Color.White, 6)
                    VGap(6)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        T("1:24", 16, Palette.OnDark60)
                        T("-2:17", 16, Palette.OnDark60)
                    }
                    VGap(18)
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Glyph(GlyphKind.Prev, 40, Color.White, Modifier.clickable { AppActions.music("previous") })
                        Glyph(if (MusicSample.isPlaying) GlyphKind.Pause else GlyphKind.Next, 48, Color.White, Modifier.clickable { AppActions.music("toggle") })
                        Glyph(GlyphKind.Next, 40, Color.White, Modifier.clickable { AppActions.music("next") })
                    }
                }
            }
        }
    }
}

object MusicSample {
    var TITLE by mutableStateOf("재생 중인 음악 없음")
    var ARTIST by mutableStateOf("")
    var isPlaying by mutableStateOf(false)
}

@Composable
fun Art(size: Int) {
    Box(Modifier.size(size.dx).clip(androidx.compose.foundation.shape.RoundedCornerShape((size / 8f).dx)).background(Palette.Art))
}
