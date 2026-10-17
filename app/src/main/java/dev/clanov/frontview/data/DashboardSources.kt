package dev.clanov.frontview.data

import java.time.Instant

data class CalendarEvent(
    val id: String,
    val title: String,
    val start: Instant,
    val end: Instant,
    val allDay: Boolean
)

data class TaskItem(
    val id: String,
    val listId: String,
    val title: String,
    val dueDate: String?,
    val completed: Boolean
)

data class MailItem(
    val id: String,
    val from: String,
    val subject: String,
    val unread: Boolean
)

data class NewsItem(
    val title: String,
    val publisher: String,
    val url: String,
    val publishedAt: Instant
)

data class Parcel(
    val carrierCode: String,
    val trackingNumber: String,
    val status: String,
    val updatedAt: Instant?
)

data class BaseballGame(
    val id: String,
    val home: String,
    val away: String,
    val homeScore: Int?,
    val awayScore: Int?,
    val status: String,
    val inning: String?
)

interface CalendarSource {
    suspend fun events(): Result<List<CalendarEvent>>
}

interface TasksSource {
    suspend fun tasks(): Result<List<TaskItem>>
    suspend fun complete(listId: String, taskId: String): Result<Unit>
}

interface GmailSource {
    suspend fun unread(): Result<List<MailItem>>
}

interface NewsSource {
    suspend fun headlines(): Result<List<NewsItem>>
}

interface DeliverySource {
    suspend fun track(carrierCode: String, trackingNumber: String): Result<Parcel>
}

interface BaseballSource {
    suspend fun games(date: String): Result<List<BaseballGame>>
}
