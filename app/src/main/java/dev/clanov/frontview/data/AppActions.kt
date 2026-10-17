package dev.clanov.frontview.data

import dev.clanov.frontview.ui.sample.TaskRow

object AppActions {
    var signIn: () -> Unit = {}
    var googleConnected: () -> Boolean = { false }
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
