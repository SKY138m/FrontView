package dev.clanov.frontview.data

import dev.clanov.frontview.ui.sample.TaskRow

object AppActions {
    var signIn: () -> Unit = {}
    var completeTask: (TaskRow) -> Unit = {}
    var addTask: () -> Unit = {}
    var music: (String) -> Unit = {}
    var openUrl: (String) -> Unit = {}
    var configureParcel: () -> Unit = {}
}
