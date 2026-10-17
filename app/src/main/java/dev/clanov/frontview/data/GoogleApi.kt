package dev.clanov.frontview.data

object GoogleApi {
    const val CALENDAR_EVENTS = "https://www.googleapis.com/calendar/v3/calendars/primary/events"
    const val TASK_LISTS = "https://tasks.googleapis.com/tasks/v1/users/@me/lists"
    const val GMAIL_MESSAGES = "https://gmail.googleapis.com/gmail/v1/users/me/messages"

    const val CALENDAR_SCOPE = "https://www.googleapis.com/auth/calendar.events.readonly"
    const val TASKS_SCOPE = "https://www.googleapis.com/auth/tasks"
    const val GMAIL_SCOPE = "https://www.googleapis.com/auth/gmail.metadata"
}
