package dev.clanov.frontview.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.*
import dev.clanov.frontview.ui.sample.*

@Composable
fun GeminiWidget(size: SlotSize) {
    Tile(size, Palette.Gemini) {
        when (size) {
            SlotSize.M -> Row(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Orb(72)
                HGap(16)
                Column(Modifier.weight(1f)) {
                    T("Gemini", 28, Palette.GeminiInk, 700)
                    T("무엇이든 물어보세요", 18, Palette.Muted)
                }
                Glyph(GlyphKind.Mic, 32, Palette.GeminiInk)
            }
            SlotSize.L -> Column(
                Modifier.fillMaxSize().padding(24.dx),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Orb(140)
                VGap(16)
                T("Gemini", 30, Palette.GeminiInk, 700)
                VGap(10)
                Row(
                    Modifier.panel(Color.White.copy(alpha = 0.8f), 28).padding(horizontal = 22.dx, vertical = 10.dx),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    T("무엇이든 물어보세요", 18, Palette.Muted)
                    HGap(12)
                    Glyph(GlyphKind.Mic, 24, Palette.GeminiInk)
                }
            }
            SlotSize.XL -> Row(
                Modifier.fillMaxSize().padding(32.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Orb(200)
                HGap(36)
                Column(Modifier.fillMaxWidth()) {
                    T("Gemini", 36, Palette.GeminiInk, 700)
                    VGap(14)
                    SampleData.geminiPrompts.forEach { p ->
                        Box(
                            Modifier.padding(bottom = 8.dx).panel(Color.White.copy(alpha = 0.8f), 22)
                                .padding(horizontal = 20.dx, vertical = 10.dx)
                        ) { T(p, 20, Palette.Ink) }
                    }
                }
            }
        }
    }
}

@Composable
fun Orb(size: Int) {
    Box(
        Modifier.size(size.dx).drawBehind {
            drawCircle(Palette.orb(this.size.width))
        }
    )
}
