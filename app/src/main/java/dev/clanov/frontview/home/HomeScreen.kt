package dev.clanov.frontview.home

import androidx.activity.compose.BackHandler
import dev.clanov.frontview.data.AppActions
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import dev.clanov.frontview.ui.DESIGN_HEIGHT
import dev.clanov.frontview.ui.DESIGN_WIDTH
import dev.clanov.frontview.ui.DesignCanvas
import dev.clanov.frontview.ui.Glyph
import dev.clanov.frontview.ui.GlyphKind
import dev.clanov.frontview.ui.Palette
import dev.clanov.frontview.ui.SlotSize
import dev.clanov.frontview.ui.T
import dev.clanov.frontview.ui.VGap
import dev.clanov.frontview.ui.dx
import dev.clanov.frontview.ui.panel
import dev.clanov.frontview.ui.pxH
import dev.clanov.frontview.ui.pxW
import dev.clanov.frontview.ui.screens.CalendarScreen
import dev.clanov.frontview.ui.screens.ClockScreen
import dev.clanov.frontview.ui.screens.DeliveryScreen
import dev.clanov.frontview.ui.screens.GmailScreen
import dev.clanov.frontview.ui.screens.InstaScreen
import dev.clanov.frontview.ui.screens.KboScreen
import dev.clanov.frontview.ui.screens.MusicScreen
import dev.clanov.frontview.ui.screens.NewsScreen
import dev.clanov.frontview.ui.screens.TasksScreen
import dev.clanov.frontview.ui.screens.WeatherScreen
import dev.clanov.frontview.ui.widgets.CalendarWidget
import dev.clanov.frontview.ui.widgets.ClockWidget
import dev.clanov.frontview.ui.widgets.DeliveryWidget
import dev.clanov.frontview.ui.widgets.GeminiWidget
import dev.clanov.frontview.ui.widgets.GmailWidget
import dev.clanov.frontview.ui.widgets.InstaWidget
import dev.clanov.frontview.ui.widgets.KboWidget
import dev.clanov.frontview.ui.widgets.MusicWidget
import dev.clanov.frontview.ui.widgets.NewsWidget
import dev.clanov.frontview.ui.widgets.TasksWidget
import dev.clanov.frontview.ui.widgets.WeatherWidget

const val GEMINI_PACKAGE = "com.google.android.apps.bard"
const val INSTAGRAM_PACKAGE = "com.instagram.android"

@Composable
fun HomeScreen(
    layout: HomeLayout,
    onPlace: (Int, Widget) -> Unit,
    onOpenApp: (String) -> Unit
) {
    var fullScreen by rememberSaveable { mutableStateOf<Widget?>(null) }
    var editing by rememberSaveable { mutableStateOf(false) }
    var pickerIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var pendingGoogleWidget by rememberSaveable { mutableStateOf<Widget?>(null) }
    LaunchedEffect(AppActions.googleConnected) {
        if (AppActions.googleConnected) {
            pendingGoogleWidget?.let { fullScreen = it }
            pendingGoogleWidget = null
        }
    }

    BackHandler(enabled = pickerIndex != null || fullScreen != null || editing) {
        when {
            pickerIndex != null -> pickerIndex = null
            fullScreen != null -> fullScreen = null
            else -> editing = false
        }
    }

    DesignCanvas {
        layout.slots.forEachIndexed { index, widget ->
            val size = HomeLayout.SIZES[index]
            val (x, y) = HomeLayout.POSITIONS[index]
            Box(
                Modifier
                    .offset(x.dx, y.dx)
                    .size(size.pxW.dx, size.pxH.dx)
                    .clip(RoundedCornerShape(24.dx))
                    .clickable {
                        when {
                            editing -> pickerIndex = index
                            widget == Widget.GEMINI -> onOpenApp(GEMINI_PACKAGE)
                            (widget == Widget.GMAIL || widget == Widget.CALENDAR || widget == Widget.TASKS) &&
                                !AppActions.googleConnected -> {
                                pendingGoogleWidget = widget
                                AppActions.signIn()
                            }
                            (widget == Widget.MUSIC || widget == Widget.INSTAGRAM) &&
                                !AppActions.notificationsEnabled() -> AppActions.openNotifications()
                            widget == Widget.DELIVERY && dev.clanov.frontview.ui.sample.SampleData.parcels.firstOrNull()?.name == "운송장 등록" ->
                                AppActions.configureParcel()
                            widget.hasFullScreen -> fullScreen = widget
                        }
                    }
            ) {
                if (widget == Widget.EMPTY) {
                    if (editing) EmptySlot()
                } else {
                    WidgetContent(widget, size)
                    if (!editing && !AppActions.googleConnected &&
                        (widget == Widget.GMAIL || widget == Widget.CALENDAR || widget == Widget.TASKS)
                    ) {
                        Box(
                            Modifier.align(Alignment.BottomCenter)
                                .padding(bottom = 12.dx)
                                .clip(RoundedCornerShape(16.dx))
                                .background(Palette.Tile)
                                .border(1.dx, Palette.Blue, RoundedCornerShape(16.dx))
                                .clickable {
                                    pendingGoogleWidget = widget
                                    AppActions.signIn()
                                }
                                .padding(horizontal = 16.dx, vertical = 9.dx)
                        ) {
                            T("Google 계정 연결", 18, Palette.Blue, 700)
                        }
                    }
                }
                if (editing) {
                    Box(
                        Modifier
                            .matchParentSize()
                            .border(3.dx, Palette.Lavender, RoundedCornerShape(24.dx))
                    )
                }
            }
        }

        Box(
            Modifier
                .offset((DESIGN_WIDTH - 28 - 56).dx, (DESIGN_HEIGHT - 8 - 56).dx)
                .size(56.dx)
                .clip(CircleShape)
                .background(if (editing) Palette.Lavender else Palette.Ink.copy(alpha = 0.55f))
                .clickable {
                    editing = !editing
                    pickerIndex = null
                },
            contentAlignment = Alignment.Center
        ) {
            Glyph(if (editing) GlyphKind.Check else GlyphKind.Swap, 28, Color.White)
        }

        fullScreen?.let { widget ->
            Box(Modifier.fillMaxSize()) {
                FullScreenContent(widget, { fullScreen = null }, { onOpenApp(INSTAGRAM_PACKAGE) })
            }
        }

        pickerIndex?.let { index ->
            WidgetPicker(
                current = layout.slots[index],
                onPick = {
                    onPlace(index, it)
                    pickerIndex = null
                },
                onDismiss = { pickerIndex = null }
            )
        }
    }
}

@Composable
private fun BoxScope.EmptySlot() {
    Box(
        Modifier
            .matchParentSize()
            .background(Palette.Placeholder)
            .border(2.dx, Palette.PlaceholderBorder, RoundedCornerShape(24.dx)),
        contentAlignment = Alignment.Center
    ) {
        Glyph(GlyphKind.Plus, 40, Palette.PlaceholderBorder)
    }
}

@Composable
private fun WidgetContent(widget: Widget, size: SlotSize) {
    when (widget) {
        Widget.CLOCK -> ClockWidget(size)
        Widget.WEATHER -> WeatherWidget(size)
        Widget.CALENDAR -> CalendarWidget(size)
        Widget.TASKS -> TasksWidget(size)
        Widget.MUSIC -> MusicWidget(size)
        Widget.GEMINI -> GeminiWidget(size)
        Widget.GMAIL -> GmailWidget(size)
        Widget.INSTAGRAM -> InstaWidget(size)
        Widget.NEWS -> NewsWidget(size)
        Widget.DELIVERY -> DeliveryWidget(size)
        Widget.KBO -> KboWidget(size)
        Widget.EMPTY -> Unit
    }
}

@Composable
private fun FullScreenContent(widget: Widget, onClose: () -> Unit, onOpenInstagram: () -> Unit) {
    when (widget) {
        Widget.CLOCK -> ClockScreen(onClose)
        Widget.WEATHER -> WeatherScreen(onClose)
        Widget.CALENDAR -> CalendarScreen(onClose)
        Widget.TASKS -> TasksScreen(onClose)
        Widget.MUSIC -> MusicScreen(onClose)
        Widget.GMAIL -> GmailScreen(onClose)
        Widget.INSTAGRAM -> InstaScreen(onClose, onOpenInstagram)
        Widget.NEWS -> NewsScreen(onClose)
        Widget.DELIVERY -> DeliveryScreen(onClose)
        Widget.KBO -> KboScreen(onClose)
        Widget.GEMINI, Widget.EMPTY -> Unit
    }
}

@Composable
private fun WidgetPicker(current: Widget, onPick: (Widget) -> Unit, onDismiss: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f))
            .clickable(onClick = onDismiss)
    ) {
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dx, topEnd = 32.dx))
                .background(Palette.Background)
                .clickable(enabled = false) {}
                .padding(28.dx)
        ) {
            T("위젯 선택", 28, Palette.Ink, 700)
            VGap(16)
            Widget.entries.chunked(3).forEach { row ->
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 12.dx),
                    horizontalArrangement = Arrangement.spacedBy(12.dx)
                ) {
                    row.forEach { w ->
                        Column(
                            Modifier
                                .weight(1f)
                                .panel(if (w == current) Palette.LavenderLight else Palette.Tile, 20)
                                .clickable { onPick(w) }
                                .padding(18.dx)
                        ) {
                            T(w.label, 22, Palette.Ink, 700)
                            T(w.note, 15, Palette.Muted)
                        }
                    }
                }
            }
            VGap(12)
            Row(
                Modifier.fillMaxWidth()
                    .panel(Palette.Tile, 18)
                    .clickable(onClick = AppActions.signIn)
                    .padding(20.dx),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                T("Google 계정", 22, Palette.Ink, 700)
                T(if (AppActions.googleConnected) "다른 계정 연결" else "로그인 / 연결", 18, Palette.Blue, 600)
            }
            VGap(12)
        }
    }
}
