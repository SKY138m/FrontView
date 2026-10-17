package dev.clanov.frontview.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.*
import dev.clanov.frontview.ui.sample.*

@Composable
fun KboWidget(size: SlotSize) {
    Tile(size, Palette.Navy) {
        when (size) {
            SlotSize.M, SlotSize.L -> Column(
                Modifier.fillMaxSize().padding(horizontal = 24.dx, vertical = if (size == SlotSize.M) 14.dx else 24.dx),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(10, Palette.Live)
                    HGap(8)
                    T("LIVE · 7회 말", 16, Palette.Live, 700)
                }
                T("팀 A  3 : 2  팀 B", if (size == SlotSize.M) 28 else 40, Color.White, 700)
                if (size == SlotSize.L) {
                    VGap(6)
                    T("B2 S1 O1 · 1, 3루", 20, Palette.OnDark70)
                    VGap(14)
                }
                T("투 : ${SampleData.PITCHER}        타 : ${SampleData.BATTER}", if (size == SlotSize.M) 18 else 22, Palette.OnDark90, 600)
            }
            SlotSize.XL -> Row(Modifier.fillMaxSize().padding(28.dx), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Dot(10, Palette.Live)
                        HGap(8)
                        T("LIVE · 7회 말", 18, Palette.Live, 700)
                    }
                    T("팀 A  3 : 2  팀 B", 44, Color.White, 700)
                    T("B2 S1 O1 · 1, 3루", 20, Palette.OnDark70)
                }
                HGap(20)
                Column(Modifier.weight(1f)) {
                    PlayerCard("투수", SampleData.PITCHER)
                    VGap(10)
                    PlayerCard("타자", SampleData.BATTER)
                }
            }
        }
    }
}

@Composable
private fun PlayerCard(role: String, name: String) {
    Row(
        Modifier.fillMaxWidth().panel(Palette.Glass, 16).padding(horizontal = 20.dx, vertical = 14.dx),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        T(role, 18, Palette.OnDark70)
        T(name, 28, Color.White, 700)
    }
}
