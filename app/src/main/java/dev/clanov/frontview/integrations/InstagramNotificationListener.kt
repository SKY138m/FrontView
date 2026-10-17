package dev.clanov.frontview.integrations

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.compose.runtime.Immutable
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
    }

    fun remove(key: String) {
        mutable.value = mutable.value.filterNot { it.key == key }
    }
}

class InstagramNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn?.packageName != "com.instagram.android") return
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

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn?.packageName == "com.instagram.android") InstagramAlerts.remove(sbn.key)
    }
}
