package dev.clanov.frontview.integrations

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager

class AppleMusicSession(context: Context) {
    private val manager = context.getSystemService(MediaSessionManager::class.java)

    fun active(listener: ComponentName): MediaController? =
        try {
            manager.getActiveSessions(listener)
                .firstOrNull { it.packageName == "com.apple.android.music" }
        } catch (_: SecurityException) {
            null
        }
}
