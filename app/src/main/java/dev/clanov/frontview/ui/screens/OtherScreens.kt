package dev.clanov.frontview.ui.screens

import androidx.compose.foundation.background
import dev.clanov.frontview.data.AppActions
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.*
import dev.clanov.frontview.ui.sample.*
import dev.clanov.frontview.ui.widgets.*

@Composable
fun GmailScreen(onClose: () -> Unit) {
    val unread = SampleData.mails.count { it.unread }
    FullFrame(Palette.Background, onClose) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            T("받은편지함", 44, Palette.Ink, 700)
            HGap(14)
            CountBadge("$unread", Palette.MailRed, Color.White, 40)
        }
        VGap(14)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dx)) {
            listOf("전체", "안 읽음", "별표").forEachIndexed { i, c ->
                Box(Modifier.panel(if (i == 0) Palette.MailRed else Palette.Tile, 22).padding(horizontal = 22.dx, vertical = 10.dx)) {
                    T(c, 20, if (i == 0) Color.White else Palette.Ink, 600)
                }
            }
        }
        VGap(12)
        SampleData.mails.forEach { m ->
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dx), verticalAlignment = Alignment.CenterVertically) {
                if (m.unread) Dot(14, Palette.Blue) else HGap(14)
                HGap(16)
                Column(Modifier.weight(1f)) {
                    T(m.from, 22, Palette.Ink, if (m.unread) 700 else 500)
                    T(m.subject, 22, Palette.Ink, if (m.unread) 600 else 400)
                    T(m.preview, 18, Palette.Muted)
                }
                HGap(10)
                T(m.time, 18, Palette.Faint)
            }
            Box(Modifier.fillMaxWidth().height(1.dx).background(Palette.Hairline))
        }
    }
}

@Composable
fun InstaScreen(onClose: () -> Unit, onOpenApp: () -> Unit) {
    val unread = SampleData.dms.count { it.unread }
    FullFrame(Palette.Background, onClose) {
        Column(Modifier.fillMaxWidth().panel(Color.White, 28).background(Palette.Insta).padding(28.dx)) {
            T("Instagram DM", 32, Color.White, 700)
            Row(verticalAlignment = Alignment.CenterVertically) {
                T("$unread", 88, Color.White, 700)
                HGap(12)
                T("개의 새 메시지", 24, Palette.OnDark85)
            }
        }
        VGap(12)
        T("알림을 누르면 Instagram으로 이동합니다", 18, Palette.Muted)
        VGap(8)
        SampleData.dms.forEach { d ->
            Row(
                Modifier.fillMaxWidth().clickable(onClick = onOpenApp).padding(vertical = 12.dx),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Avatar(d.initial, 64, Palette.LavenderLight, Palette.LavenderDark)
                HGap(16)
                Column(Modifier.weight(1f)) {
                    T(d.name, 24, Palette.Ink, if (d.unread) 700 else 500)
                    T(d.message, 20, Palette.Muted)
                }
                T(d.time, 18, Palette.Faint)
            }
        }
    }
}

@Composable
fun NewsScreen(onClose: () -> Unit) {
    FullFrame(Palette.Background, onClose) {
        T("Google 뉴스", 44, Palette.Ink, 700)
        VGap(12)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dx)) {
            SampleData.newsChips.forEachIndexed { i, c ->
                Box(Modifier.panel(if (i == 0) Palette.Blue else Palette.Tile, 22).padding(horizontal = 22.dx, vertical = 10.dx)) {
                    T(c, 20, if (i == 0) Color.White else Palette.Ink, 600)
                }
            }
        }
        VGap(16)
        Column(Modifier.fillMaxWidth().panel(Palette.Blue, 24).background(Palette.NewsFeature).clickable { AppActions.openUrl(SampleData.newsFeature.url) }.padding(28.dx)) {
            T(SampleData.newsFeature.source, 18, Palette.OnDark85)
            VGap(6)
            T(SampleData.newsFeature.title, 34, Color.White, 700, maxLines = 3)
        }
        VGap(10)
        SampleData.news.forEach { n ->
            Column(Modifier.fillMaxWidth().clickable { AppActions.openUrl(n.url) }.padding(vertical = 12.dx)) {
                T(n.title, 26, Palette.Ink, 600, maxLines = 2)
                T(n.source, 18, Palette.Muted)
            }
            Box(Modifier.fillMaxWidth().height(1.dx).background(Palette.Hairline))
        }
    }
}

@Composable
fun DeliveryScreen(onClose: () -> Unit) {
    val main = SampleData.parcels.first()
    FullFrame(Palette.Grouped, onClose) {
        T("배송조회", 44, Palette.Ink, 700)
        VGap(14)
        Column(Modifier.fillMaxWidth().panel(Color.White, 24).clickable { AppActions.configureParcel() }.padding(24.dx)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                T(main.name, 30, Palette.Ink, 700)
                T(main.status, 24, Palette.Orange, 700)
            }
            T(main.sub, 18, Palette.Muted)
            VGap(14)
            SampleData.trackSteps.forEach { s ->
                Row(Modifier.padding(vertical = 8.dx), verticalAlignment = Alignment.CenterVertically) {
                    Dot(if (s.current) 22 else 16, if (s.reached) Palette.Orange else Palette.Divider)
                    HGap(18)
                    Column(Modifier.weight(1f)) {
                        T(s.label, 24, if (s.reached) Palette.Ink else Palette.Faint, if (s.current) 700 else 500)
                        T(s.place, 17, Palette.Muted)
                    }
                    T(s.time, 16, Palette.Faint)
                }
            }
        }
        VGap(16)
        T("다른 택배", 24, Palette.Ink, 700)
        VGap(8)
        SampleData.parcels.drop(1).forEach { p ->
            Column(Modifier.fillMaxWidth().padding(bottom = 10.dx).panel(Color.White, 20).padding(20.dx)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    T(p.name, 24, Palette.Ink, 700)
                    T(p.status, 20, if (p.delivered) Palette.Green else Palette.Orange, 600)
                }
                T(p.sub, 16, Palette.Muted)
                VGap(8)
                Segments(p.progress, 5, if (p.delivered) Palette.Green else Palette.Orange, Palette.Divider, 8)
            }
        }
    }
}

@Composable
fun KboScreen(onClose: () -> Unit) {
    FullFrame(Palette.Navy, onClose, dark = true) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Dot(12, Palette.Live)
            HGap(10)
            T(SampleData.statusLine, 22, Palette.Live, 700)
        }
        T(SampleData.scoreLine, 64, Color.White, 700)
        T(SampleData.countLine, 24, Palette.OnDark70)
        VGap(18)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dx)) {
            listOf("투수" to SampleData.PITCHER, "타자" to SampleData.BATTER).forEach { (role, name) ->
                Column(Modifier.weight(1f).panel(Palette.Glass, 20).padding(20.dx)) {
                    T(role, 18, Palette.OnDark70)
                    T(name, 34, Color.White, 700)
                }
            }
        }
        VGap(18)
        Column(Modifier.fillMaxWidth().panel(Palette.GlassSoft, 20).padding(18.dx)) {
            ScoreLine("", SampleData.innings, true)
            ScoreLine("A", SampleData.inningsAway, false)
            ScoreLine("B", SampleData.inningsHome, false)
        }
        VGap(18)
        T("오늘 경기", 24, Color.White, 700)
        VGap(6)
        SampleData.games.forEach { g ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 8.dx),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                T(g.match, 22, Color.White, 500)
                T(g.state, 20, if (g.live) Palette.Live else Palette.OnDark70, 600)
            }
        }
        VGap(14)
        T("순위", 24, Color.White, 700)
        VGap(6)
        SampleData.standings.forEach { s ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dx)) {
                T(s.rank, 22, Palette.OnDark70, modifier = Modifier.width(40.dx))
                T(s.team, 22, Color.White, 600, modifier = Modifier.weight(1f))
                T(s.record, 20, Palette.OnDark85, modifier = Modifier.width(150.dx))
                T(s.pct, 20, Palette.OnDark85)
            }
        }
    }
}

@Composable
private fun ScoreLine(label: String, cells: List<String>, head: Boolean) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dx)) {
        T(label, 20, Palette.OnDark70, 700, modifier = Modifier.width(40.dx))
        cells.forEach { c ->
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                T(c, 20, if (head) Palette.OnDark60 else Color.White, if (head) 400 else 600)
            }
        }
    }
}
