package dev.clanov.frontview

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.clanov.frontview.data.AppActions
import dev.clanov.frontview.data.GoogleConnection
import dev.clanov.frontview.data.PublicSources
import dev.clanov.frontview.data.TrackingSettings
import dev.clanov.frontview.home.HomeScreen
import dev.clanov.frontview.home.LauncherSettings
import dev.clanov.frontview.home.LauncherViewModel
import dev.clanov.frontview.integrations.AppleMusicSession
import dev.clanov.frontview.integrations.InstagramNotificationListener
import dev.clanov.frontview.ui.widgets.MusicSample
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    private val model: LauncherViewModel by viewModels {
        LauncherViewModel.Factory(LauncherSettings(applicationContext))
    }
    private val sources = PublicSources()
    private lateinit var google: GoogleConnection
    private lateinit var tracking: TrackingSettings
    private val notificationComponent by lazy {
        ComponentName(this, InstagramNotificationListener::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        google = GoogleConnection(this) {
            lifecycleScope.launch { google.refresh() }
        }
        tracking = TrackingSettings(this)
        AppActions.googleConnected = { google.isAuthorized }
        AppActions.signIn = { google.signIn() }
        AppActions.completeTask = { task ->
            lifecycleScope.launch {
                runCatching { google.completeTask(task) }.onFailure(::showError)
            }
        }
        AppActions.addTask = { showAddTaskDialog() }
        AppActions.configureParcel = { showParcelDialog() }
        AppActions.setNewsTopic = { topic -> lifecycleScope.launch { runCatching { sources.news(topic) }.onFailure(::showError) } }
        AppActions.openCalendarNewEvent = { AppActions.openUrl("https://calendar.google.com/calendar/u/0/r/eventedit") }
        AppActions.notificationsEnabled = {
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
                ?.contains(packageName) == true
        }
        AppActions.openNotifications = {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        AppActions.openUrl = { url ->
            if (url.startsWith("https://")) startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
        AppActions.music = { action ->
            val session = AppleMusicSession(this).active(notificationComponent)
            when (action) {
                "previous" -> session?.transportControls?.skipToPrevious()
                "next" -> session?.transportControls?.skipToNext()
                "toggle" -> if (MusicSample.isPlaying)
                    session?.transportControls?.pause() else session?.transportControls?.play()
            }
        }

        setContent {
            val layout by model.layout.collectAsStateWithLifecycle()
            MaterialTheme { HomeScreen(layout, model::place, ::openApp) }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    while (isActive) {
                        runCatching { sources.weather() }.onFailure(::showError)
                        delay(20 * 60 * 1000L)
                    }
                }
                launch {
                    while (isActive) {
                        runCatching { sources.news() }.onFailure(::showError)
                        delay(30 * 60 * 1000L)
                    }
                }
                launch {
                    while (isActive) {
                        runCatching { sources.baseball() }.onFailure(::showError)
                        delay(45_000)
                    }
                }
                launch {
                    while (isActive) {
                        runCatching { google.refresh() }.onFailure(::showError)
                        delay(5 * 60 * 1000L)
                    }
                }
                launch {
                    while (isActive) {
                        if (tracking.isConfigured()) {
                            runCatching {
                                sources.track(tracking.key(), tracking.carrier(), tracking.invoice())
                            }.onFailure(::showError)
                        }
                        delay(3 * 60 * 60 * 1000L)
                    }
                }
                launch {
                    val music = AppleMusicSession(this@HomeActivity)
                    while (isActive) {
                        val session = music.active(notificationComponent)
                        val metadata = session?.metadata
                        MusicSample.TITLE = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)
                            ?: "재생 중인 음악 없음"
                        MusicSample.ARTIST = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST).orEmpty()
                        MusicSample.isPlaying = session?.playbackState?.state == PlaybackState.STATE_PLAYING
                        MusicSample.bitmap = metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)
                            ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
                        val duration = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION) ?: 0L
                        val position = session?.playbackState?.position ?: 0L
                        MusicSample.progress = if (duration > 0)
                            (position.toFloat() / duration).coerceIn(0f, 1f) else 0f
                        MusicSample.elapsed = durationText(position)
                        MusicSample.remaining = "-" + durationText((duration - position).coerceAtLeast(0L))
                        delay(2_000)
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Android")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        google.handleResult(requestCode, data)
    }

    private fun showAddTaskDialog() {
        if (!google.isAuthorized) { google.signIn(); return }
        val input = EditText(this).apply { hint = "할 일 제목" }
        AlertDialog.Builder(this).setTitle("새 할 일").setView(input)
            .setNegativeButton("취소", null)
            .setPositiveButton("추가") { _, _ ->
                lifecycleScope.launch {
                    runCatching { google.addTask(input.text.toString()) }.onFailure(::showError)
                }
            }.show()
    }

    private fun showParcelDialog() {
        val key = EditText(this).apply { hint = "스마트택배 API 키"; setText(tracking.key()) }
        val carrier = EditText(this).apply { hint = "택배사 코드"; setText(tracking.carrier()) }
        val invoice = EditText(this).apply { hint = "운송장 번호"; setText(tracking.invoice()) }
        val fields = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 16, 32, 16)
            addView(key); addView(carrier); addView(invoice)
        }
        AlertDialog.Builder(this).setTitle("배송조회 설정").setView(fields)
            .setNegativeButton("취소", null)
            .setPositiveButton("저장") { _, _ ->
                tracking.save(key.text.toString().trim(), carrier.text.toString().trim(),
                    invoice.text.toString().trim())
                lifecycleScope.launch {
                    runCatching {
                        sources.track(tracking.key(), tracking.carrier(), tracking.invoice())
                    }.onFailure(::showError)
                }
            }.show()
    }

    private fun durationText(ms: Long): String {
        val seconds = (ms / 1000).coerceAtLeast(0L)
        return (seconds / 60).toString() + ":" + (seconds % 60).toString().padStart(2, '0')
    }

    private fun openApp(packageName: String) {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) startActivity(intent)
        else Toast.makeText(this, "앱을 설치해 주세요", Toast.LENGTH_SHORT).show()
    }

    private fun showError(error: Throwable) {
        android.util.Log.w("FrontView", error.javaClass.simpleName)
    }

    override fun onDestroy() {
        super.onDestroy()
        google.close()
        AppActions.signIn = {}
        AppActions.openUrl = {}
        AppActions.music = {}
        AppActions.completeTask = {}
        AppActions.addTask = {}
        AppActions.configureParcel = {}
        AppActions.setNewsTopic = {}
        AppActions.openCalendarNewEvent = {}
        AppActions.openNotifications = {}
    }
}
