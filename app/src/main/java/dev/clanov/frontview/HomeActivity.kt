package dev.clanov.frontview

import android.content.ComponentName
import android.media.MediaMetadata
import android.media.session.PlaybackState
import android.os.Bundle
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
import dev.clanov.frontview.data.PublicSources
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val layout by model.layout.collectAsStateWithLifecycle()
            MaterialTheme { HomeScreen(layout, model::place, ::openApp) }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    while (isActive) {
                        runCatching { sources.weather() }
                        delay(20 * 60 * 1000L)
                    }
                }
                launch {
                    while (isActive) {
                        runCatching { sources.news() }
                        delay(30 * 60 * 1000L)
                    }
                }
                launch {
                    while (isActive) {
                        runCatching { sources.baseball() }
                        delay(45_000)
                    }
                }
                launch {
                    val music = AppleMusicSession(this@HomeActivity)
                    val listener = ComponentName(this@HomeActivity, InstagramNotificationListener::class.java)
                    while (isActive) {
                        val session = music.active(listener)
                        val metadata = session?.metadata
                        MusicSample.TITLE = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)
                            ?: "재생 중인 음악 없음"
                        MusicSample.ARTIST = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST).orEmpty()
                        MusicSample.isPlaying = session?.playbackState?.state == PlaybackState.STATE_PLAYING
                        delay(2_000)
                    }
                }
            }
        }
    }

    private fun openApp(packageName: String) {
        packageManager.getLaunchIntentForPackage(packageName)?.let(::startActivity)
    }
}
