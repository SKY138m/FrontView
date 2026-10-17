package dev.clanov.frontview.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LauncherViewModel(private val settings: LauncherSettings) : ViewModel() {
    val slots = settings.slots.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        QuickSlots()
    )

    fun selectSecondary(slot: QuickSlot) {
        viewModelScope.launch { settings.setSecondary(slot) }
    }

    class Factory(private val settings: LauncherSettings) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LauncherViewModel(settings) as T
    }
}
