package dev.clanov.frontview.data

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Base64
import dev.clanov.frontview.BuildConfig
import dev.clanov.frontview.ui.Palette
import dev.clanov.frontview.ui.sample.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import kotlin.coroutines.resume

class GoogleConnection(private val activity: Activity, private val onAuthorized: () -> Unit) {
    private val service = AuthorizationService(activity)
    private val client = OkHttpClient()
    private val stateStore = AuthCipher(activity)
    private var state = stateStore.read()?.let {
        runCatching { AuthState.jsonDeserialize(it) }.getOrNull()
    } ?: AuthState()

    val isAuthorized: Boolean get() = state.isAuthorized

    fun signIn() {
        if (BuildConfig.GOOGLE_CLIENT_ID.isBlank()) return
        val config = AuthorizationServiceConfiguration(
            Uri.parse("https://accounts.google.com/o/oauth2/v2/auth"),
            Uri.parse("https://oauth2.googleapis.com/token")
        )
        val request = AuthorizationRequest.Builder(
            config,
            BuildConfig.GOOGLE_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(BuildConfig.GOOGLE_REDIRECT_URI)
        ).setScopes(
            GoogleApi.CALENDAR_SCOPE,
            GoogleApi.TASKS_SCOPE,
            "https://www.googleapis.com/auth/gmail.readonly"
        ).build()
        activity.startActivityForResult(service.getAuthorizationRequestIntent(request), REQUEST_CODE)
    }

    fun handleResult(requestCode: Int, data: Intent?) {
        if (requestCode != REQUEST_CODE || data == null) return
        val response = AuthorizationResponse.fromIntent(data)
        val error = AuthorizationException.fromIntent(data)
        state.update(response, error)
        save()
        if (response != null) {
            service.performTokenRequest(response.createTokenExchangeRequest()) { token, ex ->
                state.update(token, ex)
                save()
                if (state.isAuthorized) activity.runOnUiThread(onAuthorized)
            }
        }
    }

    private fun save() {
        stateStore.write(state.jsonSerializeString())
    }

    private suspend fun accessToken(): String? = suspendCancellableCoroutine { continuation ->
        if (!state.isAuthorized) {
            continuation.resume(null)
            return@suspendCancellableCoroutine
        }
        state.performActionWithFreshTokens(service) { token, _, error ->
            save()
            if (continuation.isActive) continuation.resume(if (error == null) token else null)
        }
    }

    private fun call(token: String, url: String, method: String = "GET", body: JSONObject? = null): JSONObject {
        val builder = Request.Builder().url(url).header("Authorization", "Bearer " + token)
        if (method != "GET") builder.method(method, (body ?: JSONObject()).toString()
            .toRequestBody("application/json".toMediaType()))
        return client.newCall(builder.build()).execute().use { response ->
            check(response.isSuccessful) { "Google API HTTP " + response.code }
            JSONObject(checkNotNull(response.body).string())
        }
    }

    suspend fun refresh() {
        val token = accessToken() ?: return
        withContext(Dispatchers.IO) {
            runCatching { refreshCalendar(token) }
            runCatching { refreshTasks(token) }
            runCatching { refreshMail(token) }
        }
    }

    private fun refreshCalendar(token: String) {
        val now = LocalDate.now()
        val url = HttpUrl.Builder().scheme("https").host("www.googleapis.com")
            .addPathSegments("calendar/v3/calendars/primary/events")
            .addQueryParameter("timeMin", now.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime().toString())
            .addQueryParameter("timeMax", now.plusDays(32).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime().toString())
            .addQueryParameter("singleEvents", "true").addQueryParameter("orderBy", "startTime")
            .addQueryParameter("maxResults", "100").build()
        val result = call(token, url.toString()).optJSONArray("items") ?: return
        val events = (0 until result.length()).map { result.getJSONObject(it) }
            .filter { it.optString("status") != "cancelled" }
        val currentMonth = now.year.toString() + "-" + now.monthValue.toString().padStart(2, '0')
        val dots = mutableMapOf<Int, List<androidx.compose.ui.graphics.Color>>()
        val rows = events.mapNotNull { event ->
            val start = event.optJSONObject("start") ?: return@mapNotNull null
            val date = start.optString("date").ifBlank { start.optString("dateTime").take(10) }
            if (date.startsWith(currentMonth)) {
                val day = date.takeLast(2).toIntOrNull()
                if (day != null) dots[day] = listOf(Palette.Event)
            }
            val datePart = runCatching { LocalDate.parse(date) }.getOrNull() ?: return@mapNotNull null
            val time = start.optString("dateTime").let {
                runCatching { OffsetDateTime.parse(it).atZoneSameInstant(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ofPattern("HH:mm")) }.getOrDefault("종일")
            }
            datePart to EventRow(event.optString("summary", "제목 없음"),
                datePart.format(DateTimeFormatter.ofPattern("M/d")) + " " + time, Palette.Event)
        }
        SampleData.eventDots = dots
        SampleData.events = rows.take(6).map { it.second }.ifEmpty {
            listOf(EventRow("예정된 일정 없음", "", Palette.Event))
        }
    }

    private fun refreshTasks(token: String) {
        val lists = call(token, GoogleApi.TASK_LISTS).optJSONArray("items") ?: return
        val tasks = mutableListOf<TaskRow>()
        val counts = mutableListOf<TaskCount>()
        for (i in 0 until lists.length()) {
            val list = lists.getJSONObject(i)
            val listId = list.optString("id")
            if (listId.isBlank()) continue
            val endpoint = HttpUrl.Builder().scheme("https").host("tasks.googleapis.com")
                .addPathSegments("tasks/v1/lists/" + listId + "/tasks")
                .addQueryParameter("showCompleted", "false")
                .addQueryParameter("maxResults", "100").build()
            val items = call(token, endpoint.toString()).optJSONArray("items")
            val count = items?.length() ?: 0
            counts.add(TaskCount(list.optString("title", "할 일"), count, Palette.Blue))
            if (items != null) for (j in 0 until items.length()) {
                val task = items.getJSONObject(j)
                if (task.optString("status") == "needsAction") {
                    tasks.add(TaskRow(task.optString("title"), false, task.optString("id"), listId))
                }
            }
        }
        SampleData.tasks = tasks.take(15)
        SampleData.taskCounts = counts.take(4)
        SampleData.taskLists = counts.drop(4).take(4)
    }

    private fun refreshMail(token: String) {
        val url = HttpUrl.Builder().scheme("https").host("gmail.googleapis.com")
            .addPathSegments("gmail/v1/users/me/messages")
            .addQueryParameter("labelIds", "INBOX").addQueryParameter("maxResults", "15").build()
        val messages = call(token, url.toString()).optJSONArray("messages")
        val mails = mutableListOf<MailRow>()
        if (messages != null) for (i in 0 until messages.length()) {
            val id = messages.getJSONObject(i).optString("id")
            val detail = call(token, "https://gmail.googleapis.com/gmail/v1/users/me/messages/" +
                id + "?format=metadata")
            val headers = detail.optJSONObject("payload")?.optJSONArray("headers")
            fun header(name: String): String {
                if (headers == null) return ""
                for (j in 0 until headers.length()) {
                    val h = headers.getJSONObject(j)
                    if (h.optString("name").equals(name, true)) return h.optString("value")
                }
                return ""
            }
            val labels = detail.optJSONArray("labelIds")
            val unread = labels != null && (0 until labels.length()).any { labels.optString(it) == "UNREAD" }
            mails.add(MailRow(header("From"), header("Date").take(16),
                header("Subject"), detail.optString("snippet"), unread))
        }
        SampleData.mails = mails.ifEmpty { listOf(MailRow("Gmail", "", "받은 메일 없음", "", false)) }
    }

    suspend fun completeTask(task: TaskRow) {
        if (task.id.isBlank() || task.listId.isBlank()) return
        val token = accessToken() ?: return
        withContext(Dispatchers.IO) {
            val url = "https://tasks.googleapis.com/tasks/v1/lists/" + task.listId +
                "/tasks/" + task.id
            call(token, url, "PATCH", JSONObject().put("status", "completed"))
            refreshTasks(token)
        }
    }

    suspend fun addTask(title: String) {
        if (title.isBlank()) return
        val token = accessToken() ?: return
        withContext(Dispatchers.IO) {
            val lists = call(token, GoogleApi.TASK_LISTS).optJSONArray("items") ?: return@withContext
            if (lists.length() == 0) return@withContext
            val id = lists.getJSONObject(0).optString("id")
            call(token, "https://tasks.googleapis.com/tasks/v1/lists/" + id + "/tasks",
                "POST", JSONObject().put("title", title))
            refreshTasks(token)
        }
    }

    fun close() = service.dispose()

    companion object { const val REQUEST_CODE = 9150 }
}

private class AuthCipher(private val context: android.content.Context) {
    private val prefs = context.getSharedPreferences("google_auth", android.content.Context.MODE_PRIVATE)
    private val alias = "frontview_google_oauth"

    private fun key(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val old = store.getKey(alias, null)
        if (old is SecretKey) return old
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(KeyGenParameterSpec.Builder(alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build())
        return generator.generateKey()
    }

    fun write(value: String) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key())
        val bytes = cipher.iv + cipher.doFinal(value.toByteArray(Charsets.UTF_8))
        prefs.edit().putString("state", Base64.encodeToString(bytes, Base64.NO_WRAP)).apply()
    }

    fun read(): String? = runCatching {
        val value = prefs.getString("state", null) ?: return@runCatching null
        val bytes = Base64.decode(value, Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, bytes.copyOfRange(0, 12)))
        String(cipher.doFinal(bytes.copyOfRange(12, bytes.size)), Charsets.UTF_8)
    }.getOrNull()
}
