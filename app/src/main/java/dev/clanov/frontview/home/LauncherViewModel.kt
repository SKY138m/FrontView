package dev.clanov.frontview.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LauncherViewModel(private val settings: LauncherSettings) : ViewModel() {
    val layout = settings.layout.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeLayout()
    )

    fun place(index: Int, widget: Widget) {
        viewModelScope.launch { settings.save(layout.value.place(index, widget)) }
    }

    class Factory(private val settings: LauncherSettings) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LauncherViewModel(settings) as T
    }
}
