package dev.clanov.frontview.data

import dev.clanov.frontview.ui.sample.TaskRow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppActions {
    var signIn: () -> Unit = {}
    var googleConnected by mutableStateOf(false)
    var notificationsEnabled: () -> Boolean = { false }
    var openNotifications: () -> Unit = {}
    var completeTask: (TaskRow) -> Unit = {}
    var addTask: () -> Unit = {}
    var music: (String) -> Unit = {}
    var openUrl: (String) -> Unit = {}
    var openMail: () -> Unit = {}
    var configureParcel: () -> Unit = {}
    var setNewsTopic: (String) -> Unit = {}
    var openCalendarNewEvent: () -> Unit = {}
}
