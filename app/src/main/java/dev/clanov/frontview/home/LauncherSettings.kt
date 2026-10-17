package dev.clanov.frontview.home

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.launcherStore by preferencesDataStore(name = "launcher")

class LauncherSettings(private val context: Context) {
    private val secondKey = stringPreferencesKey("second_slot")

    val slots: Flow<QuickSlots> = context.launcherStore.data.map { preferences ->
        val second = QuickSlot.entries.firstOrNull {
            it.name == preferences[secondKey] && it != QuickSlot.MUSE
        } ?: QuickSlot.GMAIL
        QuickSlots(second = second)
    }

    suspend fun setSecondary(slot: QuickSlot) {
        require(slot != QuickSlot.MUSE)
        context.launcherStore.edit { it[secondKey] = slot.name }
    }
}
