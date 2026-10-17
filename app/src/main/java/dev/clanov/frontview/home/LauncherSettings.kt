package dev.clanov.frontview.home

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.launcherStore by preferencesDataStore(name = "launcher")

class LauncherSettings(private val context: Context) {
    private val layoutKey = stringPreferencesKey("layout")

    val layout: Flow<HomeLayout> = context.launcherStore.data.map { preferences ->
        HomeLayout.parse(preferences[layoutKey])
    }

    suspend fun save(layout: HomeLayout) {
        context.launcherStore.edit { it[layoutKey] = layout.serialize() }
    }
}
