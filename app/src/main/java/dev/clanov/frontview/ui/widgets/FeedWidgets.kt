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

private fun rowsFor(size: SlotSize) = when (size) {
    SlotSize.M -> 0
    SlotSize.L -> 3
    SlotSize.XL -> 4
}

@Composable
fun GmailWidget(size: SlotSize) {
    val unread = SampleData.mails.count { it.unread }
    Tile(size, Palette.Tile) {
        if (size == SlotSize.M) {
            val m = SampleData.mails.first()
            Row(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Glyph(GlyphKind.Mail, 40, Palette.MailRed)
                HGap(16)
                Column(Modifier.weight(1f)) {
                    T("Gmail · 안 읽음 $unread", 20, Palette.MailRed, 700)
                    T(m.subject, 20, Palette.Ink, 600)
                }
            }
        } else {
            Column(Modifier.fillMaxSize().padding(24.dx)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Glyph(GlyphKind.Mail, 30, Palette.MailRed)
                    HGap(10)
                    T("Gmail", 24, Palette.MailRed, 700)
                    HGap(10)
                    CountBadge("$unread", Palette.MailRed, Color.White)
                }
                VGap(10)
                SampleData.mails.take(rowsFor(size)).forEach { m ->
                    Row(Modifier.fillMaxWidth().padding(bottom = 8.dx), verticalAlignment = Alignment.CenterVertically) {
                        if (m.unread) Dot(10, Palette.Blue) else HGap(10)
                        HGap(10)
                        Column(Modifier.weight(1f)) {
                            T(m.from, 18, Palette.Muted)
                            T(m.subject, 20, Palette.Ink, if (m.unread) 700 else 400)
                        }
                        HGap(8)
                        T(m.time, 16, Palette.Faint)
                    }
                }
            }
        }
    }
}

@Composable
fun InstaWidget(size: SlotSize) {
    val unread = SampleData.dms.count { it.unread }
    Tile(size, Palette.Insta) {
        if (size == SlotSize.M) {
            val d = SampleData.dms.first()
            Row(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(d.initial, 64, Palette.GlassStrong, Color.White)
                HGap(16)
                Column(Modifier.weight(1f)) {
                    T("${d.name} · DM $unread", 22, Color.White, 700)
                    T(d.message, 18, Palette.OnDark85)
                }
            }
        } else {
            Column(Modifier.fillMaxSize().padding(24.dx)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    T("Instagram DM", 24, Color.White, 700)
                    HGap(10)
                    CountBadge("$unread", Color.White, Palette.LavenderDark)
                }
                VGap(12)
                SampleData.dms.take(rowsFor(size)).forEach { d ->
                    Row(Modifier.fillMaxWidth().padding(bottom = 8.dx), verticalAlignment = Alignment.CenterVertically) {
                        Avatar(d.initial, 44, Palette.GlassStrong, Color.White)
                        HGap(12)
                        Column(Modifier.weight(1f)) {
                            T(d.name, 18, Color.White, if (d.unread) 700 else 500)
                            T(d.message, 16, Palette.OnDark85)
                        }
                        HGap(8)
                        T(d.time, 14, Palette.OnDark70)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsWidget(size: SlotSize) {
    Tile(size, Palette.Tile) {
        if (size == SlotSize.M) {
            Column(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalArrangement = Arrangement.Center
            ) {
                T("Google 뉴스", 18, Palette.Blue, 700)
                T(SampleData.newsFeature.title, 22, Palette.Ink, 700, maxLines = 2)
                T(SampleData.newsFeature.source, 15, Palette.Muted)
            }
        } else {
            Column(Modifier.fillMaxSize().padding(20.dx)) {
                Box(
                    Modifier.fillMaxWidth().panel(Palette.Blue, 16).background(Palette.NewsFeature)
                        .padding(16.dx)
                ) {
                    Column {
                        T("Google 뉴스", 14, Palette.OnDark85, 700)
                        T(SampleData.newsFeature.title, 22, Color.White, 700, maxLines = 2)
                    }
                }
                VGap(10)
                SampleData.news.take(if (size == SlotSize.L) 2 else 4).forEach { n ->
                    Column(Modifier.padding(bottom = 6.dx)) {
                        T(n.title, 19, Palette.Ink, 600)
                        T(n.source, 14, Palette.Muted)
                    }
                }
            }
        }
    }
}

@Composable
fun DeliveryWidget(size: SlotSize) {
    Tile(size, Palette.Tile) {
        if (size == SlotSize.M) {
            val p = SampleData.parcels.first()
            Column(
                Modifier.fillMaxSize().padding(horizontal = 24.dx),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Glyph(GlyphKind.Car, 28, Palette.Orange)
                    HGap(8)
                    T("${p.name} · ${p.status}", 22, Palette.Ink, 700)
                }
                VGap(10)
                Segments(p.progress, 5, Palette.Orange, Palette.Divider, 8)
            }
        } else {
            Column(Modifier.fillMaxSize().padding(24.dx)) {
                T("배송조회", 24, Palette.Orange, 700)
                VGap(10)
                SampleData.parcels.take(if (size == SlotSize.L) 2 else 3).forEach { p ->
                    Column(Modifier.padding(bottom = 12.dx)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            T(p.name, 22, Palette.Ink, 700)
                            T(p.status, 18, if (p.delivered) Palette.Green else Palette.Orange, 600)
                        }
                        T(p.sub, 15, Palette.Muted)
                        VGap(6)
                        Segments(p.progress, 5, if (p.delivered) Palette.Green else Palette.Orange, Palette.Divider, 8)
                    }
                }
            }
        }
    }
}
