package dev.clanov.frontview.integrations

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.compose.runtime.Immutable
import dev.clanov.frontview.ui.sample.DmRow
import dev.clanov.frontview.ui.sample.SampleData
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Immutable
data class DirectMessageAlert(
    val key: String,
    val sender: String,
    val preview: String,
    val timestamp: Long
)

object InstagramAlerts {
    private val mutable = MutableStateFlow<List<DirectMessageAlert>>(emptyList())
    val alerts = mutable.asStateFlow()

    fun put(alert: DirectMessageAlert) {
        mutable.value = (listOf(alert) + mutable.value.filterNot { it.key == alert.key }).take(20)
        syncUi()
    }

    private fun syncUi() {
        SampleData.dms = mutable.value.map {
            DmRow(it.sender.take(1), it.sender,
                DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault()).format(Instant.ofEpochMilli(it.timestamp)),
                it.preview, true)
        }
    }

    fun remove(key: String) {
        mutable.value = mutable.value.filterNot { it.key == key }
        syncUi()
    }
}

class InstagramNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn?.packageName != "com.instagram.android") return
        val notification = sbn.notification
        if (notification.category != Notification.CATEGORY_MESSAGE &&
            notification.extras.getCharSequence(Notification.EXTRA_TEXT).isNullOrBlank()) return
        val data = sbn.notification.extras
        InstagramAlerts.put(
            DirectMessageAlert(
                key = sbn.key,
                sender = data.getCharSequence("android.title")?.toString().orEmpty(),
                preview = data.getCharSequence("android.text")?.toString().orEmpty(),
                timestamp = sbn.postTime
            )
        )
    }

    override fun onListenerConnected() {
        activeNotifications?.filter { it.packageName == "com.instagram.android" }?.forEach(::onNotificationPosted)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn?.packageName == "com.instagram.android") InstagramAlerts.remove(sbn.key)
    }
}
